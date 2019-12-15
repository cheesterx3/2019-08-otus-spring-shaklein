package ru.otus.study.spring.integrationtask.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Configuration
@RequiredArgsConstructor
@IntegrationComponentScan
public class ServiceConfig {

    @Bean
    public MessageChannel fireDataChannel() {
        return MessageChannels.publishSubscribe("fire").get();
    }

    @Bean
    public MessageChannel policeDataChannel() {
        return MessageChannels.publishSubscribe("police").get();
    }

    @Bean
    public MessageChannel serviceDataChannel() {
        return MessageChannels.publishSubscribe("service").get();
    }

    @Bean
    public MessageChannel incorrectDataChannel() {
        return MessageChannels.publishSubscribe("incorrect").get();
    }

    @Bean
    public IntegrationFlow fireDataFlow() {
        return IntegrationFlows.from("fireDataChannel")
                .handle("fireDepartmentService", "process", e -> e.id("fireDepartmentServiceEndPoint"))
                .gateway("mongoSaveChannel")
                .get();
    }

    @Bean
    public IntegrationFlow policeDataFlow() {
        return IntegrationFlows.from("policeDataChannel")
                .handle("policeProcessService", "process", e -> e.id("policeServiceEndPoint"))
                .gateway("accidentSaveChannel")
                .get();
    }

    @Bean
    public IntegrationFlow serviceDataFlow() {
        return IntegrationFlows.from("serviceDataChannel")
                .handle("technicalProcessService", "process", e -> e.id("technicalServiceEndPoint"))
                .gateway("accidentSaveChannel")
                .get();
    }

    @Bean
    public IntegrationFlow incorrectDataFlow() {
        return IntegrationFlows.from("incorrectDataChannel")
                .handle("incorrectDataProcessService","process", e -> e.id("incorrectServiceEndPoint"))
                .get();
    }
}
