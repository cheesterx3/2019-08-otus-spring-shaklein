package ru.otus.study.spring.service;

import ru.otus.study.spring.dao.TaskDao;
import ru.otus.study.spring.domain.*;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TaskCheckServiceImpl implements TaskCheckService {
    private final TestResultPresenter resultPresenter;
    private final TaskDao taskDao;

    public TaskCheckServiceImpl(TestResultPresenter resultPresenter, TaskDao taskDao) {
        this.resultPresenter = resultPresenter;
        this.taskDao = taskDao;

    }

    @Override
    public void checkAnswersAndShowResults(Collection<StudentAnswer> answers, StudentNameInfo studentNameInfo) {
        int correctTasks = 0;
        for (StudentAnswer answer : answers) {
            if (checkTask(answer)) correctTasks++;
        }
        resultPresenter.showResults(studentNameInfo, MessageFormat.format("You have answered correctly on {0} questions", correctTasks));
    }

    private boolean checkTask(StudentAnswer studentAnswer) {
        List<Answer> correctAnswers = taskDao.getCorrectAnswers(studentAnswer.getStudentTask());
        return answersAreSame(correctAnswers,studentAnswer.getReadOnlyAnswers());
    }

    private boolean answersAreSame(Collection<Answer> answersToCheck, Collection<Answer> sourceAnswers) {
        if (Objects.isNull(answersToCheck)||Objects.isNull(sourceAnswers))
            return false;
        return sourceAnswers.size() == answersToCheck.size() && sourceAnswers.containsAll(answersToCheck);
    }
}
