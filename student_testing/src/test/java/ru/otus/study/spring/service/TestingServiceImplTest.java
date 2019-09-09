package ru.otus.study.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.study.spring.dao.TaskDao;
import ru.otus.study.spring.domain.Answer;
import ru.otus.study.spring.domain.StudentAnswer;
import ru.otus.study.spring.domain.StudentTask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Методы сервиса проведения теста должны")
@ExtendWith(MockitoExtension.class)
class TestingServiceImplTest {
    private final List<StudentTask> taskList = Arrays.asList(new StudentTask("Question 1"), new StudentTask("Question 2"));

    @Mock
    private TaskDao taskDao;
    @Mock
    private UserInteractionService interactionService;

    private TestingService testingService;

    @BeforeEach
    void setUp() {
        given(taskDao.getTasks()).willReturn(taskList);
        given(interactionService.askTask(taskList.get(0))).willReturn(any());
        given(interactionService.askTask(taskList.get(1))).willReturn(any());
        testingService=new TestingServiceImpl(taskDao,interactionService);
    }

    @Test
    @DisplayName("выдать приветствие с использованием interactionService. Метод processTestingAndGetReadOnlyResults")
    void processTestingGreeting() {
        testingService.processTestingAndGetReadOnlyResults();
        verify(interactionService,times(1)).greetUser();
    }

    @Test
    @DisplayName("вывести список вопросов из taskDao. Метод processTestingAndGetReadOnlyResults")
    void processTestingAndAskTasks() {
        testingService.processTestingAndGetReadOnlyResults();
        verify(interactionService,times(2)).askTask(any());
    }

    @Test
    @DisplayName("получить not null список ответов. Метод processTestingAndGetReadOnlyResults")
    void processTestingAndReceiveAnswersNotNull() {
        final List<StudentAnswer> answers=testingService.processTestingAndGetReadOnlyResults();
        assertNotNull(answers);
    }

    @Test
    @DisplayName("получить список наборов ответов, соответствующий кол-ву вопросов. Метод processTestingAndGetReadOnlyResults")
    void processTestingAndReceiveAnswersCorrectCount() {
        final List<StudentAnswer> answers=testingService.processTestingAndGetReadOnlyResults();
        assertEquals(answers.size(),taskList.size());
    }



}