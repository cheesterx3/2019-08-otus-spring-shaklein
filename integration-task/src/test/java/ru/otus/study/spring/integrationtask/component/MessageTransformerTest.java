package ru.otus.study.spring.integrationtask.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.study.spring.integrationtask.domain.*;
import ru.otus.study.spring.integrationtask.service.SensorRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = MessageTransformer.class)
@DisplayName("Конвертер сообщений ")
class MessageTransformerTest {

    public static final String CORRECT_CODE = "CORRECT_CODE";
    public static final String INCORRECT_CODE = "INCORRECT_CODE";
    public static final String TEST_MESSAGE = "TEST MESSAGE";
    public static final String SENSOR_CODE = "SensorCode";
    @MockBean
    private SensorRepository sensorRepository;
    @Mock
    private Sensor sensor;
    @Mock
    private SensorMessage sensorMessage;
    @Mock
    private MonitoringObject monitoringObject;
    @Autowired
    private MessageTransformer transformer;

    @BeforeEach
    void setUp() {
        given(sensor.getObject()).willReturn(monitoringObject);
        given(sensorRepository.findSensorByCode(eq(CORRECT_CODE))).willReturn(Optional.of(sensor));
        given(sensorRepository.findSensorByCode(eq(INCORRECT_CODE))).willReturn(Optional.empty());
        given(sensorMessage.getMessage()).willReturn(TEST_MESSAGE);
        given(sensorMessage.getType()).willReturn(MessageType.Alarm);
        given(sensorMessage.getSensorId()).willReturn(SENSOR_CODE);
    }

    @Test
    @DisplayName(" должен возвращать корректное сообщение с привязанным объектом")
    void shouldReturnCorrectObjectMessage() {
        given(sensorMessage.getSensorId()).willReturn(CORRECT_CODE);
        final ObjectMessage objectMessage = transformer.transform(sensorMessage);
        assertThat(objectMessage)
                .isNotNull()
                .matches(message -> message.getMessage().equals(TEST_MESSAGE))
                .matches(message -> message.getType() == MessageType.Alarm)
                .matches(message -> message.getObject() == monitoringObject);
    }

    @Test
    @DisplayName(" должен возвращать сообщение без объекта, если сенсор не привязан ни к чему")
    void shouldReturnFailedObjectMessageOnIncorrectData() {
        given(sensorMessage.getSensorId()).willReturn(INCORRECT_CODE);
        final ObjectMessage objectMessage = transformer.transform(sensorMessage);
        assertThat(objectMessage)
                .isNotNull()
                .matches(message -> message.getMessage().equals(TEST_MESSAGE))
                .matches(message -> message.getType() == MessageType.Alarm)
                .matches(message -> message.getObject() == null);
    }
}