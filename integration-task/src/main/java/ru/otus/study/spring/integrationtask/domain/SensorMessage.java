package ru.otus.study.spring.integrationtask.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SensorMessage {
    private MessageType type;
    private String message;
    private String sensorId;
}
