package ru.otus.study.spring.integrationtask.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.study.spring.integrationtask.domain.AbstractAccident;

@MessagingGateway
public interface SensorDataTransducer {

    @Gateway(requestChannel = "dataChannel",replyChannel = "accidentChannel")
    AbstractAccident process(String sensorData);
}
