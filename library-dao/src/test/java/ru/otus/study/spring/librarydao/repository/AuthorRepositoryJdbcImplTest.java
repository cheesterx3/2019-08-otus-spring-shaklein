package ru.otus.study.spring.librarydao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.study.spring.librarydao.model.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Репозиторий работы с авторами на основе Jdbc ")
@JdbcTest
@Import({AuthorRepositoryJdbcImpl.class})
class AuthorRepositoryJdbcImplTest {
    private final static int EXPECTED_ROW_COUNT = 3;

    @Autowired
    private AuthorRepositoryJdbcImpl repositoryJdbc;

    @Test
    @DisplayName(" должен возвращать корректное кол-во строк")
    void count() {
        final int count = repositoryJdbc.count();
        assertEquals(EXPECTED_ROW_COUNT, count);
    }

    @Test
    @DisplayName(" должен возвращать автора по его идентификатору")
    void getExistingById() {
        final Author author = repositoryJdbc.getById(2);
        assertThat(author).isNotNull().matches(a -> a.getName().equals("Author2"));
    }

    @Test
    @DisplayName(" должен возвращать null в случае запроса по идентификатору при его отсутствии")
    void getMissingById() {
        final Author author = repositoryJdbc.getById(6);
        assertThat(author).isNull();
    }

    @Test
    @DisplayName(" должен возвращать корректный список авторов")
    void getAll() {
        final List<Author> authors = repositoryJdbc.getAll();
        assertThat(authors).isNotNull().hasSize(EXPECTED_ROW_COUNT);
    }

    @Test
    @DisplayName(" должен корректно удалять автора по идентификтаору при его наличии и возвращать true")
    void deleteById() {
        final boolean deleted = repositoryJdbc.deleteById(1);
        final int count = repositoryJdbc.count();
        assertTrue(deleted);
        assertThat(count).isEqualTo(EXPECTED_ROW_COUNT - 1);
    }

    @Test
    @DisplayName(" должен корректно добавлять автора по имени и возвращать его экземпляр со сгенерированным идентификатором")
    void insert() {
        final String authorName = "SomeAuthor";
        final Author author = repositoryJdbc.insert(authorName);
        final int count = repositoryJdbc.count();
        assertThat(author)
                .isNotNull()
                .matches(a -> a.getId() > 0)
                .matches(a -> a.getName().equals(authorName));
        assertThat(count).isEqualTo(EXPECTED_ROW_COUNT + 1);
    }
}