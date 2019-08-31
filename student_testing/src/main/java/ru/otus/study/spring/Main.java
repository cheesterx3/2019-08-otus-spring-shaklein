package ru.otus.study.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.study.spring.service.AppRunner;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("/context.xml");
        AppRunner testService = context.getBean(AppRunner.class);
        testService.run();
    }
}
