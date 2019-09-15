package ru.otus.study.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.study.spring.dao.TaskDao;
import ru.otus.study.spring.domain.Answer;
import ru.otus.study.spring.domain.StudentTask;
import ru.otus.study.spring.service.i18n.LocalizationService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Методы сервиса опроса студентов должны ")
@SpringBootTest(classes = UserInteractionServiceImpl.class)
class UserInteractionServiceImplTest {
    private final static String RESELECT_MSG = "message.reselect";
    private final List<StudentTask> taskList = Arrays.asList(new StudentTask("Question 1"), new StudentTask("Question 2"));
    private final List<Answer> firstTaskAnswers = Arrays.asList(new Answer("1"), new Answer("2"));

    @MockBean
    private TaskDao taskDao;
    @MockBean
    private IOService ioService;
    @MockBean
    @Qualifier("messageLocalizationService")
    private LocalizationService localizationService;

    @Autowired
    private UserInteractionService interactionService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("выдать пользователю вопрос со списком ответов, если у вопроса есть список ответов. Метод askTask")
    void askTaskWithAnswers() {
        final StudentTask task = taskList.get(0);
        given(taskDao.getAnswerVariants(task)).willReturn(firstTaskAnswers);
        given(ioService.tryToReadIntValues()).willReturn(Arrays.asList(1));
        interactionService.askTask(task);
        verify(ioService, times(1)).printOutput(task.getQuestion());
        verify(ioService, times(1)).printOutput(MessageFormat.format("\t{0}. {1}", 1, firstTaskAnswers.get(0).getContent()));
        verify(ioService, times(1)).printOutput(MessageFormat.format("\t{0}. {1}", 2, firstTaskAnswers.get(1).getContent()));
    }

    @Test
    @DisplayName("пропустить вопрос, если у вопроса нет ответов. Метод askTask")
    void askTaskWithNoneAnswers() {
        final StudentTask task = taskList.get(0);
        given(taskDao.getAnswerVariants(task)).willReturn(Collections.emptyList());
        interactionService.askTask(task);
        verify(ioService, times(0)).printOutput(task.getQuestion());
    }

    @Test
    @DisplayName("повторить запрос на ввод ответа, если ответ введён некорректно. Метод askTask")
    void askTaskWithEmptyAnswers() {
        given(localizationService.getLocalized(RESELECT_MSG)).willReturn("RESELECT");
        final StudentTask task = taskList.get(0);
        given(taskDao.getAnswerVariants(task)).willReturn(firstTaskAnswers);
        given(ioService.tryToReadIntValues()).willReturn(new ArrayList<>(), Arrays.asList(1));
        interactionService.askTask(task);
        verify(ioService, times(1)).printOutput("RESELECT");
    }

    @Test
    @DisplayName("повторить запрос на ввод ответа, если номер ответа введён некорректно. Метод askTask")
    void askTaskWithWrongIndexAnswers() {
        given(localizationService.getLocalized(RESELECT_MSG)).willReturn("RESELECT");
        final StudentTask task = taskList.get(0);
        final List<Integer> list = new ArrayList<>();
        list.add(5);
        given(taskDao.getAnswerVariants(task)).willReturn(firstTaskAnswers);
        given(ioService.tryToReadIntValues()).willReturn(list, Arrays.asList(1));
        interactionService.askTask(task);
        verify(ioService, times(1)).printOutput("RESELECT");
    }

}