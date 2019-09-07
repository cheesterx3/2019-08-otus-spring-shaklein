package ru.otus.study.spring.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.dao.TaskDao;
import ru.otus.study.spring.domain.Answer;
import ru.otus.study.spring.domain.StudentAnswer;
import ru.otus.study.spring.domain.StudentTask;
import ru.otus.study.spring.service.i18n.LocalizationService;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserInteractionServiceImpl implements UserInteractionService {
    private final static String RESELECT_MSG = "message.reselect";
    private final static String GREET_MSG = "message.greet";

    private final TaskDao taskDao;
    private final IOService ioService;
    private final LocalizationService messageLocalizationService;

    public UserInteractionServiceImpl(TaskDao taskDao, IOService ioService,
                                      @Qualifier("messageLocalizationService") LocalizationService messageLocalizationService) {
        this.taskDao = taskDao;
        this.ioService = ioService;

        this.messageLocalizationService = messageLocalizationService;
    }

    @Override
    public StudentAnswer askTask(StudentTask studentTask) {
        final Map<Integer, Answer> taskAnswers = showQuestionAndGetAnswerVariants(studentTask);
        if (!taskAnswers.isEmpty()) {
            showAnswers(taskAnswers);
            Set<Answer> answeredList = readStudentAnswers(taskAnswers);
            return new StudentAnswer(studentTask, answeredList);
        }
        return new StudentAnswer(studentTask, Collections.emptySet());
    }

    @Override
    public void greetUser() {
        ioService.printOutput(messageLocalizationService.getLocalized(GREET_MSG));
    }

    private Map<Integer, Answer> showQuestionAndGetAnswerVariants(StudentTask studentTask) {
        List<Answer> answerList = taskDao.getAnswerVariants(studentTask);
        ioService.printOutput(studentTask.getQuestion());
        return convertAnswersListToMap(answerList);
    }

    private void showAnswers(Map<Integer, Answer> answers) {
        answers.forEach((id, answer) -> ioService.printOutput(MessageFormat.format("\t{0}. {1}", id, answer.getContent())));
    }

    private Set<Answer> readStudentAnswers(Map<Integer, Answer> answers) {
        final Set<Answer> answersList = new HashSet<>();
        List<Integer> answerIndexes = tryReadAnswerAndGetIndexes(answers.size());
        answerIndexes.forEach(index -> answersList.add(answers.get(index)));
        return answersList;
    }

    private Map<Integer, Answer> convertAnswersListToMap(List<Answer> answerList) {
        return answerList.stream().filter(Objects::nonNull).collect(Collectors.toMap(answer -> answerList.indexOf(answer) + 1, answer -> answer));
    }

    private List<Integer> tryReadAnswerAndGetIndexes(int maxAnswerIndex) {
        final List<Integer> indexList = tryParseToReadAnswerIndexes(maxAnswerIndex);
        if (indexList.isEmpty()) {
            return repeatAnswerReading(maxAnswerIndex);
        }
        return indexList;
    }

    private List<Integer> repeatAnswerReading(int maxAnswerIndex) {
        ioService.printOutput(messageLocalizationService.getLocalized(RESELECT_MSG));
        return tryReadAnswerAndGetIndexes(maxAnswerIndex);
    }

    private List<Integer> tryParseToReadAnswerIndexes(int maxAnswerIndex) {
        List<Integer> indexList = ioService.tryToReadIntValues();
        indexList.removeIf(index -> isIndexOutOfBounds(maxAnswerIndex, index));
        return indexList;
    }

    private boolean isIndexOutOfBounds(int maxAnswerIndex, Integer index) {
        return index <= 0 || index > maxAnswerIndex;
    }

}
