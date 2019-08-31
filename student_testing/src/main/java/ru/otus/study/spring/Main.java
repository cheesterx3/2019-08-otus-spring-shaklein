package ru.otus.study.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.study.spring.domain.StudentAnswer;
import ru.otus.study.spring.domain.StudentNameInfo;
import ru.otus.study.spring.service.StudentInfoAskService;
import ru.otus.study.spring.service.TaskCheckService;
import ru.otus.study.spring.service.TestingService;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("/context.xml");
        TestingService testService = context.getBean(TestingService.class);
        TaskCheckService checkService = context.getBean(TaskCheckService.class);
        StudentInfoAskService studentInfoAskService=context.getBean(StudentInfoAskService.class);
        StudentNameInfo studentNameInfo=studentInfoAskService.getStudentInfo();
        if(studentNameInfo!=null) {
            Iterable<StudentAnswer> answers = testService.processTestingAndGetResults();
            checkService.checkAnswersAndShowResults(answers, studentNameInfo);
        }
    }
}
