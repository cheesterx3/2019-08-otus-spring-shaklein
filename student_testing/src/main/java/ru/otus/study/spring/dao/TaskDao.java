package ru.otus.study.spring.dao;

import ru.otus.study.spring.domain.Answer;
import ru.otus.study.spring.domain.StudentTask;

import java.util.List;

public interface TaskDao {
    List<StudentTask> getTasks();

    List<Answer> getAnswerVariants(StudentTask task);

    List<Answer> getCorrectAnswers(StudentTask studentTask);
}
