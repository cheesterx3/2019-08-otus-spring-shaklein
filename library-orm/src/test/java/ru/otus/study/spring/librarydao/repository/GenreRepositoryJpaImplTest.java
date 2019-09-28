package ru.otus.study.spring.librarydao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.study.spring.librarydao.helper.GenericDaoResult;
import ru.otus.study.spring.librarydao.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий работы с жанрами на основе Jpa ")
@DataJpaTest
@Import({GenreRepositoryJpaImpl.class})
class GenreRepositoryJpaImplTest {
    private final static String GENRE_NAME_TO_FIND="Genre2";

    @Autowired
    private GenreRepositoryJpaImpl genreRepositoryJpa;
    @Autowired
    private TestEntityManager em;


    @Test
    @DisplayName(" должен возвращать жанр по его идентификатору")
    void getExistingById() {
        final Optional<Genre> genre = genreRepositoryJpa.getById(2);
        assertThat(genre).isNotEmpty().matches(g -> g.get().getName().equals("Genre2"));
        final Genre expectedGenre = em.find(Genre.class, 2L);
        assertThat(genre).isPresent().get()
                .isEqualToComparingFieldByFieldRecursively(expectedGenre);
    }

    @Test
    @DisplayName(" должен возвращать пустой optional в случае запроса по идентификатору при его отсутствии")
    void getMissingById() {
        final Optional<Genre> genre = genreRepositoryJpa.getById(6);
        assertThat(genre).isEmpty();
    }

    @Test
    @DisplayName(" должен возвращать жанр по наименованию")
    void getExistingByName() {
        final GenericDaoResult<Genre> genreByName = genreRepositoryJpa.getByName(GENRE_NAME_TO_FIND);
        assertThat(genreByName.getResult()).isNotEmpty().matches(g -> g.get().getName().equals("Genre2"));
    }

    @Test
    @DisplayName(" должен корректно добавлять жанр по имени и возвращать его экземпляр со сгенерированным идентификатором")
    void insert() {
        final String genreName = "Some genre";
        final GenericDaoResult<Genre> genreInsertResult = genreRepositoryJpa.insert(genreName);
        assertThat(genreInsertResult.getResult())
                .isPresent()
                .matches(g -> g.get().getId() > 0)
                .matches(g -> g.get().getName().equals(genreName));
        final Genre actualGenre = em.find(Genre.class, genreInsertResult.getResult().get().getId());
        assertThat(actualGenre)
                .isNotNull()
                .matches(g -> g.getId() > 0)
                .matches(g -> g.getName().equals(genreName));
    }


}