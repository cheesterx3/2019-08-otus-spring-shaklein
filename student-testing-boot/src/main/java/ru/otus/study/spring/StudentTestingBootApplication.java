package ru.otus.study.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.study.spring.service.AppGreeterService;


@SpringBootApplication
public class StudentTestingBootApplication {
    private final AppGreeterService greeterService;

    public StudentTestingBootApplication(AppGreeterService greeterService) {
        this.greeterService = greeterService;
        greeterService.appGreet();
    }


    public static void main(String[] args) {
        SpringApplication.run(StudentTestingBootApplication.class, args);
    }

}
