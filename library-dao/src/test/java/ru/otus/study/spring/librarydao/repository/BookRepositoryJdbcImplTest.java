package ru.otus.study.spring.librarydao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.study.spring.librarydao.model.Author;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий работы с книгами на основе Jdbc ")
@JdbcTest
@Import({BookRepositoryJdbcImpl.class, AuthorRepositoryJdbcImpl.class, GenreRepositoryJdbcImpl.class})
class BookRepositoryJdbcImplTest {
    private final static int EXPECTED_ROW_COUNT = 4;

    @Autowired
    private BookRepositoryJdbcImpl repositoryJdbc;
    @Autowired
    private AuthorRepositoryJdbcImpl authorRepositoryJdbc;
    @Autowired
    private GenreRepositoryJdbcImpl genreRepositoryJdbc;

    @Test
    @DisplayName(" должен возвращать корректное кол-во строк")
    void count() {
        final int count = repositoryJdbc.count();
        assertEquals(EXPECTED_ROW_COUNT, count);
    }

    @Test
    @DisplayName(" должен возвращать жанр по его идентификатору")
    void getExistingById() {
        final Book book = repositoryJdbc.getById(2);
        assertThat(book)
                .isNotNull()
                .matches(b -> b.getName().equals("Book2"))
                .matches(b -> b.getGenres().size() > 0)
                .matches(b -> b.getAuthors().size() > 0);
    }

    @Test
    @DisplayName(" должен возвращать null в случае запроса по идентификатору при его отсутствии")
    void getMissingById() {
        final Book book = repositoryJdbc.getById(6);
        assertThat(book).isNull();
    }

    @Test
    @DisplayName(" должен возвращать список книг с корректно заполненными данными ")
    void getAll() {
        final List<Book> books = repositoryJdbc.getAll();
        assertThat(books).isNotNull().hasSize(EXPECTED_ROW_COUNT)
                .allMatch(b -> !b.getName().isEmpty())
                .allMatch(b -> b.getGenres().size() > 0)
                .allMatch(b -> b.getAuthors().size() > 0);
    }

    @Test
    @DisplayName(" должен корректно удалять книгу по идентификтору при его наличии и возвращать true")
    void deleteById() {
        final boolean deleted = repositoryJdbc.deleteById(1);
        final int count = repositoryJdbc.count();
        assertTrue(deleted);
        assertThat(count).isEqualTo(EXPECTED_ROW_COUNT - 1);
    }

    @Test
    @DisplayName(" должен корректно добавлять книгу по имени с установленным жанром и автором и возвращать его экземпляр со сгенерированным идентификатором")
    void insertCorrect() {
        final String bookName = "Some book";
        final Genre genre = genreRepositoryJdbc.getById(1);
        final Author author = authorRepositoryJdbc.getById(1);

        final Book book = repositoryJdbc.insert(bookName, author, genre);
        final int count = repositoryJdbc.count();
        assertThat(book)
                .isNotNull()
                .matches(b -> b.getId() > 0)
                .matches(b -> b.getName().equals(bookName))
                .matches(b -> b.getAuthors().contains(author))
                .matches(b -> b.getGenres().contains(genre));
        assertThat(count).isEqualTo(EXPECTED_ROW_COUNT + 1);
    }

    @Test
    @DisplayName(" должен выкинуть NPE если жанр или автор null")
    void insertWithAuthorOrGenreInCorrect() {
        final String bookName = "Some book";
        final Genre genre = null;
        final Author author = null;
        assertThrows(NullPointerException.class, () -> repositoryJdbc.insert(bookName, author, genre));
    }
}