package ru.otus.study.spring.integrationtask.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.*;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import ru.otus.study.spring.integrationtask.component.MessageTransformer;
import ru.otus.study.spring.integrationtask.domain.*;
import ru.otus.study.spring.integrationtask.helper.ValidationUtils;

@IntegrationComponentScan
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final MessageTransformer messageTransformer;

    @Bean
    public QueueChannel dataChannel() {
        return MessageChannels.queue(50).get();
    }

    @Bean
    public MessageChannel messageSaveChannel() {
        return MessageChannels.queue(50).get();
    }

    @Bean
    public MessageChannel accidentSaveChannel() {
        return MessageChannels.queue(50).get();
    }

    @Bean
    public MessageChannel accidentChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public MessageChannel routeChannel() {
        return MessageChannels.direct().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(10)
                .maxMessagesPerPoll(20)
                .get();
    }

    @Bean
    public IntegrationFlow sensorDataFlow() {
        return IntegrationFlows.from("dataChannel")
                .filter(ValidationUtils::isDataValid)
                .transform(Transformers.fromJson(SensorMessage.class))
                .routeToRecipients(r -> r
                        .<SensorMessage>recipientFlow(message -> message.getType() == MessageType.Unknown,
                                subFlow -> subFlow
                                        .channel("errorChannel")
                                        .<SensorMessage, AbstractAccident>transform(source -> Accident.nullAccident()))
                        .defaultOutputToParentFlow()
                )
                .transform(messageTransformer::transform, e -> e.id("objectMessageTransformEndPoint"))
                .gateway("messageSaveChannel")
                .routeToRecipients(r -> r
                        .<ObjectMessage>recipientFlow(message -> message.getObject() == null,
                                subFlow -> subFlow.channel("incorrectDataChannel"))
                        .defaultOutputToParentFlow()
                )
                .channel("routeChannel")
                .get();
    }

    @Bean
    public IntegrationFlow routeFlow() {
        return IntegrationFlows.from("routeChannel")
                .route("payload.type", r -> r
                        .channelMapping(MessageType.Fire, "fireDataChannel")
                        .channelMapping(MessageType.Alarm, "policeDataChannel")
                        .channelMapping(MessageType.Service, "serviceDataChannel")
                ).get();
    }

}
