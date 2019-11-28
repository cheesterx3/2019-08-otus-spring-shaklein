package ru.otus.study.spring.librarymvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
@EnableMongoRepositories
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties
public class LibraryMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryMvcApplication.class, args);
    }

}
