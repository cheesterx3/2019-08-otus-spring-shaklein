package ru.otus.study.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.otus.study.spring.service.IOService;
import ru.otus.study.spring.service.IOServiceImpl;

@Configuration
public class IOConfig {
    @Bean
    @Primary
    public IOService getIOService() {
        return new IOServiceImpl(System.in, System.out);
    }
}
