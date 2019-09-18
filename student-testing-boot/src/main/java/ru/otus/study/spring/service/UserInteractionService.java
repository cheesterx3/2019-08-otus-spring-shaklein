package ru.otus.study.spring.service;

import ru.otus.study.spring.domain.StudentAnswer;
import ru.otus.study.spring.domain.StudentTask;

public interface UserInteractionService {
    StudentAnswer askTask(StudentTask studentTask);

    void greetUser();
}
