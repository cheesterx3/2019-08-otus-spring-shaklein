package ru.otus.study.spring.service;

import java.util.List;

public interface IOService {
    String getUserInput();
    void printOutput(String data);
    List<Integer> tryToReadIntValues();
}
