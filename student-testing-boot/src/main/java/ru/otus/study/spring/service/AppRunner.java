package ru.otus.study.spring.service;

import ru.otus.study.spring.domain.StudentNameInfo;

public interface AppRunner {
    void run();

    void setStudentNameAndRun(StudentNameInfo studentNameInfo);
}
