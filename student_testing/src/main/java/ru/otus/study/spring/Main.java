package ru.otus.study.spring;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ru.otus.study.spring.service.AppRunner;

@PropertySource("app.properties")
@Configuration
@ComponentScan
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(Main.class);
        AppRunner testService = context.getBean(AppRunner.class);
        testService.run();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }
}
