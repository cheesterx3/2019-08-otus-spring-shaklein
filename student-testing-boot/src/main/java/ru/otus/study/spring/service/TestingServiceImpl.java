package ru.otus.study.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.dao.TaskDao;
import ru.otus.study.spring.domain.StudentAnswer;
import ru.otus.study.spring.domain.StudentTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class TestingServiceImpl implements TestingService {
    private final List<StudentAnswer> answers = new ArrayList<>();
    private final TaskDao taskDao;
    private final UserInteractionService interactionService;

    public TestingServiceImpl(TaskDao taskDao, UserInteractionService interactionService) {
        this.taskDao = taskDao;
        this.interactionService = interactionService;
    }

    public List<StudentAnswer> processTestingAndGetReadOnlyResults() {
        answers.clear();
        interactionService.greetUser();
        List<StudentTask> studentTasks = taskDao.getTasks();
        studentTasks.forEach(this::askTask);
        return Collections.unmodifiableList(answers);
    }

    private void askTask(StudentTask studentTask) {
        StudentAnswer answer = interactionService.askTask(studentTask);
        if (Objects.nonNull(answer))
            answers.add(answer);
    }
}
