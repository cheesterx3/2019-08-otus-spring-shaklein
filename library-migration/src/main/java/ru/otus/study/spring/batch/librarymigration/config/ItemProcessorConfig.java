package ru.otus.study.spring.batch.librarymigration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.study.spring.batch.librarymigration.component.DomainTransformer;
import ru.otus.study.spring.batch.librarymigration.domain.jdbc.*;
import ru.otus.study.spring.batch.librarymigration.domain.mongo.*;

@Configuration
@RequiredArgsConstructor
public class ItemProcessorConfig {
    private final DomainTransformer domainTransformer;

    @Bean
    public ItemProcessor<Author, AuthorMongo> authorProcessor() {
        return domainTransformer::toMongoAuthor;
    }

    @Bean
    public ItemProcessor<Genre, GenreMongo> genreProcessor() {
        return domainTransformer::toMongoGenre;
    }

    @Bean
    public ItemProcessor<Book, BookMongo> bookProcessor() {
        return domainTransformer::toMongoBook;
    }

    @Bean
    public ItemProcessor<Role, RoleMongo> roleProcessor() {
        return domainTransformer::toMongoRole;
    }

    @Bean
    public ItemProcessor<User, UserMongo> userProcessor() {
        return domainTransformer::toMongoUser;
    }

    @Bean
    public ItemProcessor<BookComment, BookCommentMongo> bookCommentProcessor() {
        return domainTransformer::toMongoComment;
    }
}
