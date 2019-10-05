package ru.otus.study.spring.libraryspringdata.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.study.spring.libraryspringdata.domain.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий работы с жанрами ")
@DataJpaTest
class GenreRepositoryTest {
    private final static String GENRE_NAME_TO_FIND = "Genre2";

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private TestEntityManager em;


    @Test
    @DisplayName(" должен возвращать жанр по его идентификатору")
    void getExistingById() {
        final Optional<Genre> genre = genreRepository.findById(2L);
        assertThat(genre).isNotEmpty().get().matches(g -> g.getName().equals("Genre2"));
        final Genre expectedGenre = em.find(Genre.class, 2L);
        assertThat(genre).isPresent().get()
                .isEqualToComparingFieldByFieldRecursively(expectedGenre);
    }

    @Test
    @DisplayName(" должен возвращать пустой optional в случае запроса по идентификатору при его отсутствии")
    void getMissingById() {
        final Optional<Genre> genre = genreRepository.findById(6L);
        assertThat(genre).isEmpty();
    }

    @Test
    @DisplayName(" должен возвращать жанр по наименованию")
    void getExistingByName() {
        final Optional<Genre> genreByName = genreRepository.findByNameIgnoreCase(GENRE_NAME_TO_FIND);
        assertThat(genreByName).isNotEmpty().get().matches(g -> g.getName().equals("Genre2"));
    }

    @Test
    @DisplayName(" должен корректно добавлять жанр и возвращать его экземпляр со сгенерированным идентификатором")
    void insert() {
        final String genreName = "Some genre";
        final Genre genre = genreRepository.save(new Genre(genreName));
        assertThat(genre)
                .isNotNull()
                .matches(g -> g.getId() > 0)
                .matches(g -> g.getName().equals(genreName));
        final Genre actualGenre = em.find(Genre.class, genre.getId());
        assertThat(actualGenre)
                .isNotNull()
                .matches(g -> g.getId() > 0)
                .matches(g -> g.getName().equals(genreName));
    }
}