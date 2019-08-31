package ru.otus.study.spring.service;

import ru.otus.study.spring.domain.StudentNameInfo;

import java.text.MessageFormat;

public class TestResultPresenterImpl implements TestResultPresenter {
    @Override
    public void showResults(StudentNameInfo studentNameInfo, String testResults) {
        System.out.println(MessageFormat.format("Dear {0}", studentNameInfo.getName()));
        System.out.println(testResults);
    }
}
