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
        List<Integer> answerIndexes = tryReadAnswer(answers.size());
        answerIndexes.forEach(index -> answersList.add(answers.get(index)));
        return answersList;
    }

    private List<Integer> tryReadAnswer(int maxAnswerIndex) {
        String answer;
        if ((answer = ioService.getUserInput()) != null) {
            final List<Integer> indexList = tryParseInt(maxAnswerIndex, answer);
            if (!indexList.isEmpty())
                return indexList;
            return repeatAnswerReading(maxAnswerIndex);
        } else {
            return repeatAnswerReading(maxAnswerIndex);
        }
    }

    private List<Integer> tryParseInt(int maxAnswerIndex, String answer) {
        return new UserInputParser(answer, maxAnswerIndex).parseList();
    }

    private List<Integer> repeatAnswerReading(int maxAnswerIndex) {
        ioService.printOutput("Вы не выбрали вариант ответа. Повторите выбор.");
        return tryReadAnswer(maxAnswerIndex);
    }

    private static class UserInputParser {
        private final Scanner scanner;
        private final int maxAnswerIndex;
        private final List<Integer> indexList = new ArrayList<>();

        private UserInputParser(String userInput, int maxAnswerIndex) {
            this.maxAnswerIndex = maxAnswerIndex;
            scanner = new Scanner(userInput);
        }

        List<Integer> parseList() {
            try {
                if (scanner.hasNextInt()) {
                    tryToAddIndexToList();
                    while (scanner.hasNextInt()) {
                        tryToAddIndexToList();
                    }
                }
            } finally {
                scanner.close();
            }
            return indexList;
        }

        private void tryToAddIndexToList() {
            int ansIndex = scanner.nextInt();
            if (ansIndex > 0 && ansIndex <= maxAnswerIndex) {
                indexList.add(ansIndex);
            }
        }
    }
}
