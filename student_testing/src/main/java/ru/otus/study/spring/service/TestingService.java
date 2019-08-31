package ru.otus.study.spring.service;

import ru.otus.study.spring.domain.StudentAnswer;

import java.util.List;

public interface TestingService {

    Iterable<StudentAnswer> processTestingAndGetResults();

}
