package ru.otus.study.spring.service;

import ru.otus.study.spring.dao.TaskDao;
import ru.otus.study.spring.domain.Answer;
import ru.otus.study.spring.domain.StudentAnswer;
import ru.otus.study.spring.domain.StudentTask;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class UserInteractionServiceImpl implements UserInteractionService {
    private final TaskDao taskDao;
    private final IOService ioService;

    public UserInteractionServiceImpl(TaskDao taskDao, IOService ioService) {
        this.taskDao = taskDao;
        this.ioService = ioService;
    }

    @Override
    public StudentAnswer askTask(StudentTask studentTask) {
        final Map<Integer, Answer> taskAnswers = showQuestionAndGetAnswerVariants(studentTask);
        showAnswers(taskAnswers);
        Set<Answer> answeredList = readStudentAnswers(taskAnswers);
        return new StudentAnswer(studentTask, answeredList);
    }

    @Override
    public void greetUser() {
        ioService.printOutput("Добро пожаловать в тест. Для выбора ответа вводите его номер. Для выбора нескольких вариантов, вводите их через пробел.");
    }

    private Map<Integer, Answer> showQuestionAndGetAnswerVariants(StudentTask studentTask) {
        List<Answer> answerList = taskDao.getAnswerVariants(studentTask);
        ioService.printOutput(studentTask.getQuestion());
        return convertAnswersListToMap(answerList);
    }

    private Map<Integer, Answer> convertAnswersListToMap(List<Answer> answerList) {
        return answerList.stream().collect(Collectors.toMap(answer -> answerList.indexOf(answer) + 1, answer -> answer));
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

    private List<Integer> tryReadAnswerAndGetIndexes(int maxAnswerIndex) {
        final List<Integer> indexList = tryParseToReadAnswerIndexes(maxAnswerIndex);
        if (indexList.isEmpty()) {
            return repeatAnswerReading(maxAnswerIndex);
        }
        return indexList;
    }

    private List<Integer> repeatAnswerReading(int maxAnswerIndex) {
        ioService.printOutput("Вы не выбрали вариант ответа. Повторите выбор.");
        return tryReadAnswerAndGetIndexes(maxAnswerIndex);
    }

    private List<Integer> tryParseToReadAnswerIndexes(int maxAnswerIndex) {
        List<Integer> indexList = ioService.tryToReadIntValues();
        indexList.removeIf(index -> index <= 0 || index > maxAnswerIndex);
        return indexList;
    }

}
