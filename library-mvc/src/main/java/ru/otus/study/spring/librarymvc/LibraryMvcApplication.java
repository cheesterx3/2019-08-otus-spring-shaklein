package ru.otus.study.spring.librarymvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableConfigurationProperties
public class LibraryMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryMvcApplication.class, args);
    }

}
