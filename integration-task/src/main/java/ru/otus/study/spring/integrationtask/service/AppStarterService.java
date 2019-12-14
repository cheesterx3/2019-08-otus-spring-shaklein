package ru.otus.study.spring.integrationtask.service;

import ru.otus.study.spring.integrationtask.exceptions.AlreadyStartedException;

public interface AppStarterService {
    void start(int threadCount) throws AlreadyStartedException;

    void stop();
}
