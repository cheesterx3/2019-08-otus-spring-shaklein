package ru.otus.study.spring.batch.librarymigration.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.study.spring.batch.librarymigration.domain.mongo.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false",
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"
})
@SpringBatchTest
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "ru.otus.study.spring.batch.librarymigration.domain.jdbc",
        "ru.otus.study.spring.batch.librarymigration.config",
        "ru.otus.study.spring.batch.librarymigration.component"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Приложение по миграции данных из jdbc-базы в mongodb ")
class JobMigrationTest {
    public static final int EXPECTED_AUTHORS_COUNT = 3;
    public static final int EXPECTED_GENRES_COUNT = 4;
    public static final int EXPECTED_BOOKS_COUNT = 4;
    public static final int EXPECTED_COMMENTS_COUNT = 3;
    public static final int EXPECTED_ROLES_COUNT = 3;
    public static final int EXPECTED_USERS_COUNT = 3;
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    @DisplayName("должно корректно выполнять работу")
    void shouldCorrectlyFinishJob() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParametersBuilder().toJobParameters());
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        assertThat(actualJobInstance.getJobName()).isEqualTo("migrateJob");
        assertThat(actualJobExitStatus.getExitCode()).isEqualTo("COMPLETED");
    }

    @Test
    @DisplayName("должно корректно перенести все данные из jdbc-базы в mongodb")
    void shouldCorrectlyTransferDataToMongo() throws Exception {
        jobLauncherTestUtils.launchJob(new JobParametersBuilder().toJobParameters());
        final List<AuthorMongo> authorMongos = mongoTemplate.findAll(AuthorMongo.class);
        final List<GenreMongo> genreMongos = mongoTemplate.findAll(GenreMongo.class);
        final List<BookMongo> bookMongos = mongoTemplate.findAll(BookMongo.class);
        final List<BookCommentMongo> bookCommentMongos = mongoTemplate.findAll(BookCommentMongo.class);
        final List<RoleMongo> roleMongos = mongoTemplate.findAll(RoleMongo.class);
        final List<UserMongo> userMongos = mongoTemplate.findAll(UserMongo.class);

        assertThat(authorMongos.size()).isEqualTo(EXPECTED_AUTHORS_COUNT);
        assertThat(genreMongos.size()).isEqualTo(EXPECTED_GENRES_COUNT);
        assertThat(bookMongos.size()).isEqualTo(EXPECTED_BOOKS_COUNT);
        assertThat(bookCommentMongos.size()).isEqualTo(EXPECTED_COMMENTS_COUNT);
        assertThat(roleMongos.size()).isEqualTo(EXPECTED_ROLES_COUNT);
        assertThat(userMongos.size()).isEqualTo(EXPECTED_USERS_COUNT);
    }


}