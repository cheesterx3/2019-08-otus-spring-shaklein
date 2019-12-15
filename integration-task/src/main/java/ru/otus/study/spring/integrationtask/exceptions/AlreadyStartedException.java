package ru.otus.study.spring.integrationtask.exceptions;

public class AlreadyStartedException extends Exception {
    public AlreadyStartedException(String message) {
        super(message);
    }
}
