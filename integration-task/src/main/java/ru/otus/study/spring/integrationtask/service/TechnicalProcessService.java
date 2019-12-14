package ru.otus.study.spring.integrationtask.service;

import org.springframework.stereotype.Service;
import ru.otus.study.spring.integrationtask.domain.Accident;
import ru.otus.study.spring.integrationtask.domain.ObjectMessage;

@Service
public class TechnicalProcessService implements CommandProcessService {

    @Override
    public Accident process(ObjectMessage message) {
        return new Accident("Service accident", message);
    }
}
