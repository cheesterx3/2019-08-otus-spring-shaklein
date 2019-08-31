package ru.otus.study.spring.service;

import ru.otus.study.spring.domain.StudentAnswer;
import ru.otus.study.spring.domain.StudentNameInfo;

public interface TaskCheckService {
    void checkAnswersAndShowResults(Iterable<StudentAnswer> answers, StudentNameInfo studentNameInfo);
}
