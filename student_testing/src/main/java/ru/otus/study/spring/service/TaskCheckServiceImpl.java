package ru.otus.study.spring.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.dao.TaskDao;
import ru.otus.study.spring.domain.*;
import ru.otus.study.spring.service.i18n.LocalizationService;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class TaskCheckServiceImpl implements TaskCheckService {
    private final static String TEST_RESULT_MSG="message.testresult";

    private final TestResultPresenter resultPresenter;
    private final TaskDao taskDao;
    private final LocalizationService localizationService;


    public TaskCheckServiceImpl(TestResultPresenter resultPresenter,
                                TaskDao taskDao,
                                @Qualifier("messageLocalizationService") LocalizationService localizationService) {
        this.resultPresenter = resultPresenter;
        this.taskDao = taskDao;
        this.localizationService = localizationService;
    }

    @Override
    public void checkAnswersAndShowResults(Collection<StudentAnswer> answers, StudentNameInfo studentNameInfo) {
        int correctTasks = 0;
        for (StudentAnswer answer : answers) {
            if (checkTask(answer)) correctTasks++;
        }
        resultPresenter.showResults(studentNameInfo, getResultMessage(correctTasks));
    }

    private String getResultMessage(int correctTasks) {
        return localizationService.getLocalized(TEST_RESULT_MSG,new Integer[]{correctTasks});
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
