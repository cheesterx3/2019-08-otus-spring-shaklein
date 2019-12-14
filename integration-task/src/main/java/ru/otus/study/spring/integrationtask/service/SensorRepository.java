package ru.otus.study.spring.integrationtask.service;

import org.springframework.data.repository.Repository;
import ru.otus.study.spring.integrationtask.domain.Sensor;

import java.util.Optional;

public interface SensorRepository extends Repository<Sensor, Long> {
    Optional<Sensor> findSensorByCode(String code);
}
