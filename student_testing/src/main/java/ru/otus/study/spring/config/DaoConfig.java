package ru.otus.study.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.study.spring.dao.TaskDao;
import ru.otus.study.spring.dao.TaskCSVDaoImpl;

import java.io.InputStream;
import java.util.Objects;


@Configuration
public class DaoConfig {

    @Bean
    public TaskDao taskDao(@Value("${data.file}") String dataUrl, @Value("${app.locale}") String locale) {
        InputStream dataStream = getDataStream(dataUrl, locale);
        return new TaskCSVDaoImpl(dataStream);
    }

    private InputStream getDataStream(@Value("${data.file}") String dataUrl, @Value("${app.locale}") String locale) {
        String url = createUrlForLocale(dataUrl, locale);
        InputStream dataStream = getResourceAsStream(url);
        if (Objects.isNull(dataStream)) {
            dataStream = getResourceAsStream(dataUrl.concat(".csv"));
        }
        return dataStream;
    }

    private String createUrlForLocale(@Value("${data.file}") String dataUrl, @Value("${app.locale}") String locale) {
        return dataUrl
                .concat("_")
                .concat(locale.replaceAll("-", "_"))
                .concat(".csv");
    }

    private InputStream getResourceAsStream(String url) {
        return getClass().getResourceAsStream("/" + url);
    }

}
