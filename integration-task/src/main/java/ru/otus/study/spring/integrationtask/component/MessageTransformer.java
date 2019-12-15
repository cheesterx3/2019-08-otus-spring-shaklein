package ru.otus.study.spring.integrationtask.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.study.spring.integrationtask.domain.ObjectMessage;
import ru.otus.study.spring.integrationtask.domain.SensorMessage;
import ru.otus.study.spring.integrationtask.service.SensorRepository;

@Component("messageTransformer")
@RequiredArgsConstructor
public class MessageTransformer {
    private final SensorRepository sensorRepository;

    public ObjectMessage transform(SensorMessage sensorMessage) {
        return sensorRepository.findSensorByCode(sensorMessage.getSensorId())
                .map(sensor -> ObjectMessage.fromMessage(sensorMessage, sensor.getObject()))
                .orElse(ObjectMessage.fromMessage(sensorMessage, null));
    }
}
