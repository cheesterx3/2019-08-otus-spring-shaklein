package ru.otus.study.spring.batch.librarymigration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.study.spring.batch.librarymigration.domain.jdbc.*;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class ItemReaderConfig {
    private final EntityManagerFactory managerFactory;

    @Bean
    @StepScope
    public JpaPagingItemReader<Author> authorReader() {
        return new JpaPagingItemReaderBuilder<Author>()
                .entityManagerFactory(managerFactory)
                .queryString("select a from Author a")
                .name("authorItemReader")
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Genre> genreReader() {
        return new JpaPagingItemReaderBuilder<Genre>()
                .entityManagerFactory(managerFactory)
                .queryString("select g from Genre g")
                .name("genreItemReader")
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Role> roleReader() {
        return new JpaPagingItemReaderBuilder<Role>()
                .entityManagerFactory(managerFactory)
                .queryString("select r from Role r")
                .name("roleItemReader")
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<User> userReader() {
        return new JpaPagingItemReaderBuilder<User>()
                .entityManagerFactory(managerFactory)
                .queryString("select u from User u")
                .name("userItemReader")
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Book> bookReader() {
        return new JpaPagingItemReaderBuilder<Book>()
                .entityManagerFactory(managerFactory)
                .queryString("select b from Book b")
                .name("bookItemReader")
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<BookComment> bookCommentReader() {
        return new JpaPagingItemReaderBuilder<BookComment>()
                .entityManagerFactory(managerFactory)
                .queryString("select c from BookComment c")
                .name("bookCommentItemReader")
                .build();
    }


}
