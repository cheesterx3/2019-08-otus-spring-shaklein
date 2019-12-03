package ru.otus.study.spring.batch.librarymigration.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import ru.otus.study.spring.batch.librarymigration.domain.jdbc.*;

import java.util.ArrayList;
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
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("Конфигурация сервисов чтения ")
class ItemReaderConfigTest {
    public static final int EXPECTED_AUTHORS_COUNT = 3;
    public static final int EXPECTED_GENRES_COUNT = 4;
    public static final int EXPECTED_USER_COUNT = 3;
    public static final int EXPECTED_ROLE_COUNT = 3;
    public static final int EXPECTED_BOOK_COUNT = 4;
    public static final int EXPECTED_COMMENT_COUNT = 3;
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private JpaPagingItemReader<Author> authorReader;
    @Autowired
    private JpaPagingItemReader<Genre> genreReader;
    @Autowired
    private JpaPagingItemReader<User> userReader;
    @Autowired
    private JpaPagingItemReader<Role> roleReader;
    @Autowired
    private JpaPagingItemReader<Book> bookReader;
    @Autowired
    private JpaPagingItemReader<BookComment> bookCommentReader;

    @BeforeEach
    void clearJobExecutions() {
        this.jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    @DisplayName("должна создать reader, читающий всех авторов из БД")
    void shouldReturnAllAuthorsFromDb() throws Exception {
        checkReader(EXPECTED_AUTHORS_COUNT, authorReader);
    }

    @Test
    @DisplayName("должна создать reader, читающий все жанры")
    void shouldReturnAllGenresFromDb() throws Exception {
        checkReader(EXPECTED_GENRES_COUNT, genreReader);
    }

    @Test
    @DisplayName("должна создать reader, читающий всех пользователей")
    void shouldReturnAllUsersFromDb() throws Exception {
        checkReader(EXPECTED_USER_COUNT, userReader);
    }

    @Test
    @DisplayName("должна создать reader, читающий все роли пользователей")
    void shouldReturnAllRolesFromDb() throws Exception {
        checkReader(EXPECTED_ROLE_COUNT, roleReader);
    }

    @Test
    @DisplayName("должна создать reader, читающий все книги")
    void shouldReturnAllBOOKSFromDb() throws Exception {
        checkReader(EXPECTED_BOOK_COUNT, bookReader);
    }

    @Test
    @DisplayName("должна создать reader, читающий все комментарии")
    void shouldReturnAllCommentsFromDb() throws Exception {
        checkReader(EXPECTED_COMMENT_COUNT, bookCommentReader);
    }

    private <T> void checkReader(int expectedSize, JpaPagingItemReader<T> reader) throws Exception {
        final StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            T object;
            final List<T> objects = new ArrayList<>();
            reader.open(stepExecution.getExecutionContext());
            while ((object = reader.read()) != null) {
                objects.add(object);
            }
            reader.close();
            assertThat(objects).hasSize(expectedSize);
            return null;
        });
    }


}