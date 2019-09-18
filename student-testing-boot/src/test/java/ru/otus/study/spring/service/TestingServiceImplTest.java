package ru.otus.study.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.study.spring.dao.TaskDao;
import ru.otus.study.spring.domain.StudentAnswer;
import ru.otus.study.spring.domain.StudentTask;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Методы сервиса проведения теста должны")
@SpringBootTest(classes = TestingServiceImpl.class)
class TestingServiceImplTest {
    private final List<StudentTask> taskList = Arrays.asList(new StudentTask("Question 1"), new StudentTask("Question 2"));

    @MockBean
    private TaskDao taskDao;
    @MockBean
    private UserInteractionService interactionService;

    @Autowired
    private TestingService testingService;

    @BeforeEach
    void setUp() {
        given(taskDao.getTasks()).willReturn(taskList);
        taskList.forEach(studentTask -> given(interactionService.askTask(studentTask)).willReturn(new StudentAnswer(studentTask, Collections.emptySet())));
    }

    @Test
    @DisplayName("выдать приветствие с использованием interactionService. Метод processTestingAndGetReadOnlyResults")
    void processTestingGreeting() {
        testingService.processTestingAndGetReadOnlyResults();
        verify(interactionService, times(1)).greetUser();
    }

    @Test
    @DisplayName("вывести список вопросов из taskDao. Метод processTestingAndGetReadOnlyResults")
    void processTestingAndAskTasks() {
        testingService.processTestingAndGetReadOnlyResults();
        verify(interactionService, times(2)).askTask(any());
    }

    @Test
    @DisplayName("получить not null список ответов. Метод processTestingAndGetReadOnlyResults")
    void processTestingAndReceiveAnswersNotNull() {
        final List<StudentAnswer> answers = testingService.processTestingAndGetReadOnlyResults();
        assertNotNull(answers);
    }

    @Test
    @DisplayName("получить список наборов ответов, соответствующий кол-ву вопросов. Метод processTestingAndGetReadOnlyResults")
    void processTestingAndReceiveAnswersCorrectCount() {
        final List<StudentAnswer> answers = testingService.processTestingAndGetReadOnlyResults();
        assertEquals(taskList.size(), answers.size());
    }
}