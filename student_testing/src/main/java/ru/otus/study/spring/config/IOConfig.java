package ru.otus.study.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.study.spring.service.IOService;
import ru.otus.study.spring.service.IOServiceImpl;

import java.util.Scanner;

@Configuration
public class IOConfig {
    @Bean
    public IOService getIOService() {
        return new IOServiceImpl(System.in, System.out);
    }
}
