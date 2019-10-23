package ru.otus.study.spring.librarymvc.exception;

public class NotFoundException extends Exception {
    public NotFoundException(String s) {
        super(s);
    }

    public NotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
