package ru.otus.study.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.study.spring.dao.TaskDao;
import ru.otus.study.spring.domain.Answer;
import ru.otus.study.spring.domain.StudentAnswer;
import ru.otus.study.spring.domain.StudentTask;
import ru.otus.study.spring.service.i18n.LocalizationService;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Методы сервиса опроса студентов должны ")
@ExtendWith(MockitoExtension.class)
class UserInteractionServiceImplTest {
    private final static String RESELECT_MSG = "message.reselect";
    private final List<StudentTask> taskList = Arrays.asList(new StudentTask("Question 1"), new StudentTask("Question 2"));
    private final List<Answer> firstTaskAnswers = Arrays.asList(new Answer("1"), new Answer("2"));

    @Mock
    private TaskDao taskDao;
    @Mock
    private IOService ioService;
    @Mock
    private LocalizationService localizationService;

    private UserInteractionService interactionService;

    @BeforeEach
    void setUp() {
        interactionService = new UserInteractionServiceImpl(taskDao, ioService, localizationService);
    }

    @Test
    @DisplayName("выдать пользователю вопрос со списком ответов, если у вопроса есть список ответов. Метод askTask")
    void askTaskWithAnswers() {
        final StudentTask task=taskList.get(0);
        given(taskDao.getAnswerVariants(task)).willReturn(firstTaskAnswers);
        given(ioService.tryToReadIntValues()).willReturn(Arrays.asList(1));
        interactionService.askTask(task);
        verify(ioService,times(1)).printOutput(task.getQuestion());
        verify(ioService,times(1)).printOutput(MessageFormat.format("\t{0}. {1}", 1, firstTaskAnswers.get(0).getContent()));
        verify(ioService,times(1)).printOutput(MessageFormat.format("\t{0}. {1}", 2, firstTaskAnswers.get(1).getContent()));
        verify(ioService,times(1)).tryToReadIntValues();
    }

    @Test
    @DisplayName("пропустить вопрос, если у вопроса нет ответов. Метод askTask")
    void askTaskWithNoneAnswers() {
        final StudentTask task=taskList.get(0);
        given(taskDao.getAnswerVariants(task)).willReturn(Collections.emptyList());
        interactionService.askTask(task);
        verify(ioService,times(0)).printOutput(task.getQuestion());
    }

    @Test
    @DisplayName("повторить запрос на ввод ответа, если ответ введён некорректно. Метод askTask")
    void askTaskWithEmptyAnswers() {
        given(localizationService.getLocalized(RESELECT_MSG)).willReturn("RESELECT");
        final StudentTask task=taskList.get(0);
        given(taskDao.getAnswerVariants(task)).willReturn(firstTaskAnswers);
        given(ioService.tryToReadIntValues()).willReturn(new ArrayList<>(),Arrays.asList(1));
        interactionService.askTask(task);
        verify(ioService,times(1)).printOutput(task.getQuestion());
        verify(ioService,times(1)).printOutput(MessageFormat.format("\t{0}. {1}", 1, firstTaskAnswers.get(0).getContent()));
        verify(ioService,times(1)).printOutput(MessageFormat.format("\t{0}. {1}", 2, firstTaskAnswers.get(1).getContent()));
        verify(ioService,times(1)).printOutput("RESELECT");
        verify(ioService,times(2)).tryToReadIntValues();
    }

    @Test
    @DisplayName("повторить запрос на ввод ответа, если номер ответа введён некорректно. Метод askTask")
    void askTaskWithWrongIndexAnswers() {
        given(localizationService.getLocalized(RESELECT_MSG)).willReturn("RESELECT");
        final StudentTask task=taskList.get(0);
        final List<Integer> list=new ArrayList<>();
        list.add(5);
        given(taskDao.getAnswerVariants(task)).willReturn(firstTaskAnswers);
        given(ioService.tryToReadIntValues()).willReturn(list,Arrays.asList(1));
        interactionService.askTask(task);
        verify(ioService,times(1)).printOutput(task.getQuestion());
        verify(ioService,times(1)).printOutput(MessageFormat.format("\t{0}. {1}", 1, firstTaskAnswers.get(0).getContent()));
        verify(ioService,times(1)).printOutput(MessageFormat.format("\t{0}. {1}", 2, firstTaskAnswers.get(1).getContent()));
        verify(ioService,times(2)).tryToReadIntValues();
        verify(ioService,times(1)).printOutput("RESELECT");
    }


}