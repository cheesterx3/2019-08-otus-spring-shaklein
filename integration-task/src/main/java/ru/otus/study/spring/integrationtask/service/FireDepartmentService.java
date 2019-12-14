package ru.otus.study.spring.integrationtask.service;

import org.springframework.stereotype.Service;
import ru.otus.study.spring.integrationtask.domain.AbstractAccident;
import ru.otus.study.spring.integrationtask.domain.MongoAccident;
import ru.otus.study.spring.integrationtask.domain.ObjectMessage;

@Service
public class FireDepartmentService implements CommandProcessService {

    @Override
    public AbstractAccident process(ObjectMessage message) {
        return new MongoAccident("Fire accident", message);
    }
}
