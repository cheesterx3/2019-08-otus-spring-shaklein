package ru.otus.study.spring.batch.librarymigration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.study.spring.batch.librarymigration.domain.mongo.*;

@Configuration
@RequiredArgsConstructor
public class ItemWriterConfig {
    private final MongoTemplate mongoTemplate;

    @Bean
    public MongoItemWriter<AuthorMongo> authorWriter() {
        return new MongoItemWriterBuilder<AuthorMongo>()
                .collection("author")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public MongoItemWriter<GenreMongo> genreWriter() {
        return new MongoItemWriterBuilder<GenreMongo>()
                .collection("genre")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public MongoItemWriter<BookMongo> bookWriter() {
        return new MongoItemWriterBuilder<BookMongo>()
                .collection("book")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public MongoItemWriter<RoleMongo> roleWriter() {
        return new MongoItemWriterBuilder<RoleMongo>()
                .collection("role")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public MongoItemWriter<UserMongo> userWriter() {
        return new MongoItemWriterBuilder<UserMongo>()
                .collection("user")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public MongoItemWriter<BookCommentMongo> bookCommentWriter() {
        return new MongoItemWriterBuilder<BookCommentMongo>()
                .collection("comment")
                .template(mongoTemplate)
                .build();
    }
}
