package ru.otus.study.spring.integrationtask.service;

import ru.otus.study.spring.integrationtask.domain.AbstractAccident;
import ru.otus.study.spring.integrationtask.domain.ObjectMessage;

public interface CommandProcessService {
    AbstractAccident process(ObjectMessage message);
}
