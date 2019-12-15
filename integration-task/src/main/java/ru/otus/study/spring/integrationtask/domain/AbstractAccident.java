package ru.otus.study.spring.integrationtask.domain;

public abstract class AbstractAccident {
    public abstract String getId();

    public abstract String getInfo();

    public abstract MessageType getType();

    public abstract MonitoringObject getObject();

}
