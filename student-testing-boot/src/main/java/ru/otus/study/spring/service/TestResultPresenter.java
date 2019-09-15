package ru.otus.study.spring.service;

import ru.otus.study.spring.domain.StudentNameInfo;

public interface TestResultPresenter {
    void showResults(StudentNameInfo studentNameInfo, String testResults);
}
