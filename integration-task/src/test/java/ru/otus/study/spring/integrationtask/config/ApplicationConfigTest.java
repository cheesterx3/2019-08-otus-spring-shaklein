package ru.otus.study.spring.integrationtask.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.integration.endpoint.AbstractEndpoint;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.study.spring.integrationtask.component.MessageTransformer;
import ru.otus.study.spring.integrationtask.domain.AbstractAccident;
import ru.otus.study.spring.integrationtask.domain.MessageType;
import ru.otus.study.spring.integrationtask.domain.ObjectMessage;
import ru.otus.study.spring.integrationtask.service.SensorDataTransducer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false",
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"
})
@SpringIntegrationTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ApplicationConfigTest {
    final static String MESSAGE = String.format("{\"sensorId\":\"%s\",\"type\":\"%s\",\"message\":\"MESSAGE\"}", "001", MessageType.Alarm.name());
    final static String WRONG_MESSAGE = String.format("{\"sensorId\":\"%s\",\"type\":\"%s\",\"message\":\"MESSAGE\"}", "001", MessageType.Unknown.name());

    @Autowired
    private SensorDataTransducer dataTransducer;
    @Autowired
    @Qualifier("fireDataChannel")
    private SubscribableChannel fireDataChannel;
    @Autowired
    @Qualifier("policeDataChannel")
    private SubscribableChannel policeDataChannel;
    @Autowired
    @Qualifier("serviceDataChannel")
    private SubscribableChannel serviceDataChannel;
    @Autowired
    @Qualifier("routeChannel")
    private SubscribableChannel routeChannel;
    @Autowired
    private AbstractEndpoint fireDepartmentServiceEndPoint;
    @Autowired
    private AbstractEndpoint policeServiceEndPoint;
    @Autowired
    private AbstractEndpoint technicalServiceEndPoint;
    @MockBean
    @Qualifier("messageTransformer")
    private MessageTransformer messageTransformer;

    private ArgumentCaptor<Message<?>> messageArgumentCaptor;
    private MessageHandler mockMessageHandler;

    @BeforeEach
    void setUp() {
        messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        mockMessageHandler = mock(MessageHandler.class);
        ObjectMessage message = new ObjectMessage();
        given(messageTransformer.transform(any())).willReturn(message);
    }

    @Test
    void shouldTransformMessageInFlow() {
        dataTransducer.process(MESSAGE);
        verify(messageTransformer, times(1)).transform(any());
    }

    @Test
    void shouldCreateEmptyAccidentIfMessageIsWrong() {
        final AbstractAccident accident = dataTransducer.process(WRONG_MESSAGE);
        assertThat(accident).isNotNull()
                .matches(acc -> acc.getObject() == null);
    }

    @Test
    void shouldCreateEmptyAccidentIfObjectNotFound() {
        final AbstractAccident accident = dataTransducer.process(MESSAGE);
        assertThat(accident).isNotNull()
                .matches(acc -> acc.getObject() == null);
    }

    @Test
    void shouldRouteToFireFlow() {
        fireDepartmentServiceEndPoint.stop();
        fireDataChannel.subscribe(mockMessageHandler);

        final Message<ObjectMessage> message = createObjectMessage(MessageType.Fire);
        routeChannel.send(message);

        verify(mockMessageHandler).handleMessage(messageArgumentCaptor.capture());
        assertSame(message, messageArgumentCaptor.getValue());
    }

    @Test
    void shouldNotRouteToFireFlowWithOtherType() {
        policeServiceEndPoint.stop();
        fireDataChannel.subscribe(mockMessageHandler);

        final Message<ObjectMessage> message = createObjectMessage(MessageType.Alarm);
        routeChannel.send(message);

        verify(mockMessageHandler, never()).handleMessage(messageArgumentCaptor.capture());
    }

    @Test
    void shouldRouteToPoliceFlow() {
        policeServiceEndPoint.stop();
        policeDataChannel.subscribe(mockMessageHandler);

        final Message<ObjectMessage> message = createObjectMessage(MessageType.Alarm);
        routeChannel.send(message);

        verify(mockMessageHandler).handleMessage(messageArgumentCaptor.capture());
        assertSame(message, messageArgumentCaptor.getValue());
    }

    @Test
    void shouldNotRouteToPoliceFlowWithOtherType() {
        fireDepartmentServiceEndPoint.stop();
        policeDataChannel.subscribe(mockMessageHandler);

        routeChannel.send(createObjectMessage(MessageType.Fire));

        ArgumentCaptor<Message<?>> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(mockMessageHandler, never()).handleMessage(messageArgumentCaptor.capture());
    }

    @Test
    void shouldRouteToServiceFlow() {
        technicalServiceEndPoint.stop();
        serviceDataChannel.subscribe(mockMessageHandler);

        final Message<ObjectMessage> message = createObjectMessage(MessageType.Service);
        routeChannel.send(message);

        verify(mockMessageHandler).handleMessage(messageArgumentCaptor.capture());
        assertSame(message, messageArgumentCaptor.getValue());
    }

    @Test
    void shouldNotRouteToServiceFlowWithOtherType() {
        fireDepartmentServiceEndPoint.stop();
        serviceDataChannel.subscribe(mockMessageHandler);

        routeChannel.send(createObjectMessage(MessageType.Fire));

        ArgumentCaptor<Message<?>> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(mockMessageHandler, never()).handleMessage(messageArgumentCaptor.capture());
    }

    @Test
    void shouldNotRouteToServicesFlowWithIncorrectType() {
        fireDepartmentServiceEndPoint.stop();
        policeServiceEndPoint.stop();
        technicalServiceEndPoint.stop();

        serviceDataChannel.subscribe(mockMessageHandler);
        fireDataChannel.subscribe(mockMessageHandler);
        policeDataChannel.subscribe(mockMessageHandler);

        dataTransducer.process(WRONG_MESSAGE);

        verify(mockMessageHandler, never()).handleMessage(messageArgumentCaptor.capture());
    }

    private Message<ObjectMessage> createObjectMessage(MessageType fire) {
        final ObjectMessage objectMessage = new ObjectMessage();
        objectMessage.setType(fire);
        return MessageBuilder.withPayload(objectMessage).build();
    }

}