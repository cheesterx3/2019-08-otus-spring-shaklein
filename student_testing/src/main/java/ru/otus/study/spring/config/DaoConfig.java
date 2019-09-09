package ru.otus.study.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.study.spring.dao.TaskDao;
import ru.otus.study.spring.dao.TaskCSVDaoImpl;
import ru.otus.study.spring.service.DataUrlCreator;

import java.io.InputStream;
import java.util.Objects;


@Configuration
public class DaoConfig {

    @Bean
    public TaskDao taskDao(DataUrlCreator urlCreator) {
        InputStream dataStream = getResourceAsStream(urlCreator.createDaoURL());
        return new TaskCSVDaoImpl(dataStream);
    }

    private InputStream getResourceAsStream(String url) {
        return getClass().getResourceAsStream("/" + url);
    }

}
