package ru.otus.study.spring.service;

import ru.otus.study.spring.domain.StudentNameInfo;

import java.text.MessageFormat;

public class TestResultPresenterImpl implements TestResultPresenter {
    private final IOService ioService;

    public TestResultPresenterImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public void showResults(StudentNameInfo studentNameInfo, String testResults) {
        ioService.printOutput(MessageFormat.format("Dear {0}", studentNameInfo.getName()));
        ioService.printOutput(testResults);
    }
}
