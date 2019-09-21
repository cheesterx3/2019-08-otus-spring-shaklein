package ru.otus.study.spring.librarydao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.study.spring.librarydao.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Репозиторий работы с жанрами на основе Jdbc ")
@JdbcTest
@Import({GenreRepositoryJdbcImpl.class})
class GenreRepositoryJdbcImplTest {
    private final static int EXPECTED_ROW_COUNT = 4;

    @Autowired
    private GenreRepositoryJdbcImpl repositoryJdbc;

    @Test
    @DisplayName(" должен возвращать корректное кол-во строк")
    void count() {
        final int count = repositoryJdbc.count();
        assertEquals(EXPECTED_ROW_COUNT, count);
    }

    @Test
    @DisplayName(" должен возвращать жанр по его идентификатору")
    void getExistingById() {
        final Genre genre = repositoryJdbc.getById(2);
        assertThat(genre).isNotNull().matches(g -> g.getName().equals("Genre2"));
    }

    @Test
    @DisplayName(" должен возвращать null в случае запроса по идентификатору при его отсутствии")
    void getMissingById() {
        final Genre genre = repositoryJdbc.getById(6);
        assertThat(genre).isNull();
    }

    @Test
    @DisplayName(" должен возвращать корректный список жанров")
    void getAll() {
        final List<Genre> genres = repositoryJdbc.getAll();
        assertThat(genres).isNotNull().hasSize(EXPECTED_ROW_COUNT);
    }

    @Test
    @DisplayName(" должен корректно удалять жанр по идентификтаору при его наличии и возвращать true")
    void deleteById() {
        final boolean deleted = repositoryJdbc.deleteById(1);
        final int count = repositoryJdbc.count();
        assertTrue(deleted);
        assertThat(count).isEqualTo(EXPECTED_ROW_COUNT - 1);
    }

    @Test
    @DisplayName(" должен корректно добавлять жанр по имени и возвращать его экземпляр со сгенерированным идентификатором")
    void insert() {
        final String genreName = "Some genre";
        final Genre genre = repositoryJdbc.insert(genreName);
        final int count = repositoryJdbc.count();
        assertThat(genre)
                .isNotNull()
                .matches(g -> g.getId() > 0)
                .matches(g -> g.getName().equals(genreName));
        assertThat(count).isEqualTo(EXPECTED_ROW_COUNT + 1);
    }
}