package ru.otus.study.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.study.spring.service.AppRunner;


@SpringBootApplication
public class StudentTestingBootApplication {

    private final AppRunner appRunner;

    public StudentTestingBootApplication(AppRunner appRunner) {
        this.appRunner = appRunner;
        appRunner.run();
    }

    public static void main(String[] args) {
        SpringApplication.run(StudentTestingBootApplication.class, args);
    }

}
