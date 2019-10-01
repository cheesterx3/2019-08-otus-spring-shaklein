package ru.otus.study.spring.librarydao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.study.spring.librarydao.model.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий работы с авторами на основе Jpa ")
@DataJpaTest
@Import({AuthorRepositoryJpaImpl.class})
class AuthorRepositoryJpaImplTest {
    private final static int EXPECTED_ROW_COUNT = 3;
    
    @Autowired
    private AuthorRepository repositoryJpa;
    @Autowired
    private TestEntityManager em;


    @Test
    @DisplayName(" должен возвращать автора по его идентификатору")
    void getExistingById() {
        final Optional<Author> author = repositoryJpa.getById(2);
        final Author expectedAuthor = em.find(Author.class, 2L);
        assertThat(author).isPresent().get()
                .isEqualToComparingFieldByFieldRecursively(expectedAuthor);
    }

    @Test
    @DisplayName(" должен возвращать пустой optional в случае запроса по идентификатору при его отсутствии")
    void getMissingById() {
        final Optional<Author> author = repositoryJpa.getById(6);
        assertThat(author).isEmpty();
    }

    @Test
    @DisplayName(" должен возвращать корректный список авторов")
    void getAll() {
        final List<Author> authors = repositoryJpa.getAll();
        assertThat(authors).isNotNull().hasSize(EXPECTED_ROW_COUNT);
    }

    @Test
    @DisplayName(" должен корректно удалять автора")
    void delete() {
        final Author author = em.find(Author.class, 1L);
        repositoryJpa.delete(author);
        final Author expectedAuthor = em.find(Author.class, 1L);
        assertThat(expectedAuthor).isNull();
    }

    @Test
    @DisplayName(" должен корректно добавлять автора по имени и возвращать его экземпляр со сгенерированным идентификатором")
    void insert() {
        final String authorName = "SomeAuthor";
        final Author author = repositoryJpa.insert(new Author(authorName));
        assertThat(author)
                .isNotNull()
                .matches(a -> a.getId() > 0)
                .matches(a -> a.getName().equals(authorName));
        final Author actualAuthor = em.find(Author.class, author.getId());
        assertThat(actualAuthor).isNotNull()
                .matches(a -> a.getId() > 0)
                .matches(a -> a.getName().equals(authorName));
    }
}