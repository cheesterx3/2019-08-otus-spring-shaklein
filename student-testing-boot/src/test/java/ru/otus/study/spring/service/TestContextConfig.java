package ru.otus.study.spring.service;

import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

import java.io.ByteArrayInputStream;

import static org.mockito.BDDMockito.given;

@SpringBootConfiguration
@EnableConfigurationProperties
@ComponentScan({"ru.otus.study.spring.config", "ru.otus.study.spring.dao", "ru.otus.study.spring.service", "ru.otus.study.spring.service.i18n"})
public class TestContextConfig {

}
