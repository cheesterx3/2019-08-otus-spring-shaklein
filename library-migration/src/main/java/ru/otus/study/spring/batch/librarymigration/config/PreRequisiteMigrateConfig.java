package ru.otus.study.spring.batch.librarymigration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.study.spring.batch.librarymigration.domain.jdbc.Author;
import ru.otus.study.spring.batch.librarymigration.domain.jdbc.Genre;
import ru.otus.study.spring.batch.librarymigration.domain.jdbc.Role;
import ru.otus.study.spring.batch.librarymigration.domain.jdbc.User;
import ru.otus.study.spring.batch.librarymigration.domain.mongo.AuthorMongo;
import ru.otus.study.spring.batch.librarymigration.domain.mongo.GenreMongo;
import ru.otus.study.spring.batch.librarymigration.domain.mongo.RoleMongo;
import ru.otus.study.spring.batch.librarymigration.domain.mongo.UserMongo;


@Configuration
@RequiredArgsConstructor
public class PreRequisiteMigrateConfig {
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Flow authorMigrateStep(ItemReader<Author> authorReader,
                                  ItemProcessor<Author, AuthorMongo> authorProcessor,
                                  ItemWriter<AuthorMongo> authorWriter) {
        final TaskletStep step = stepBuilderFactory
                .get("authorMigrateStep")
                .<Author, AuthorMongo>chunk(4)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
                .allowStartIfComplete(true)
                .build();
        return new FlowBuilder<SimpleFlow>("authorFlow").start(step).build();
    }

    @Bean
    public Flow genreMigrateStep(ItemReader<Genre> genreReader,
                                 ItemProcessor<Genre, GenreMongo> genreProcessor,
                                 ItemWriter<GenreMongo> genreWriter) {
        final TaskletStep step = stepBuilderFactory.get("genreMigrateStep")
                .<Genre, GenreMongo>chunk(4)
                .reader(genreReader)
                .processor(genreProcessor)
                .writer(genreWriter)
                .allowStartIfComplete(true)
                .build();
        return new FlowBuilder<SimpleFlow>("genreFlow").start(step).build();
    }

    @Bean
    public Flow userInfoMigrateStep(Step roleMigrateStep,
                                    Step userMigrateStep) {
        return new FlowBuilder<SimpleFlow>("roleFlow")
                .start(roleMigrateStep)
                .next(userMigrateStep)
                .build();
    }

    @Bean
    public Step roleMigrateStep(ItemReader<Role> roleReader,
                                ItemProcessor<Role, RoleMongo> roleProcessor,
                                ItemWriter<RoleMongo> roleWriter) {
        return stepBuilderFactory.get("roleMigrateStep")
                .<Role, RoleMongo>chunk(4)
                .reader(roleReader)
                .processor(roleProcessor)
                .writer(roleWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step userMigrateStep(ItemReader<User> userReader,
                                ItemProcessor<User, UserMongo> userProcessor,
                                ItemWriter<UserMongo> userWriter) {
        return stepBuilderFactory.get("userMigrateStep")
                .<User, UserMongo>chunk(4)
                .reader(userReader)
                .processor(userProcessor)
                .writer(userWriter)
                .allowStartIfComplete(true)
                .build();
    }
}
