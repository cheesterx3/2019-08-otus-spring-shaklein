package ru.otus.study.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.domain.StudentAnswer;
import ru.otus.study.spring.domain.StudentNameInfo;

import java.util.List;

@Service
public class AppRunnerImpl implements AppRunner {
    private final TestingService testService;
    private final TaskCheckService checkService;
    private final StudentInfoAskService studentInfoAskService;

    public AppRunnerImpl(TestingService testService, TaskCheckService checkService, StudentInfoAskService studentInfoAskService) {
        this.testService = testService;
        this.checkService = checkService;
        this.studentInfoAskService = studentInfoAskService;
    }

    @Override
    public void run() {
        StudentNameInfo studentNameInfo=studentInfoAskService.getStudentInfo();
        if(studentNameInfo!=null) {
            List<StudentAnswer> answers = testService.processTestingAndGetReadOnlyResults();
            checkService.checkAnswersAndShowResults(answers, studentNameInfo);
        }
    }
}
