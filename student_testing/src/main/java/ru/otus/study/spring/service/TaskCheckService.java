package ru.otus.study.spring.service;

import ru.otus.study.spring.domain.StudentAnswer;
import ru.otus.study.spring.domain.StudentNameInfo;

import java.util.Collection;

public interface TaskCheckService {
    void checkAnswersAndShowResults(Collection<StudentAnswer> answers, StudentNameInfo studentNameInfo);
}
