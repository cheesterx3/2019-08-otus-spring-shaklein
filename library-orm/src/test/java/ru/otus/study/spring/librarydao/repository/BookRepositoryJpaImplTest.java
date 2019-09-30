package ru.otus.study.spring.librarydao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.study.spring.librarydao.model.Author;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Репозиторий работы с книгами на основе JPa ")
@DataJpaTest
@Import({BookRepositoryJpaImpl.class, AuthorRepositoryJpaImpl.class, GenreRepositoryJpaImpl.class})
class BookRepositoryJpaImplTest {

    @Autowired
    private BookRepositoryJpaImpl repositoryJpa;
    @Autowired
    private AuthorRepositoryJpaImpl authorRepositoryJpa;
    @Autowired
    private GenreRepositoryJpaImpl genreRepositoryJpa;
    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName(" должен возвращать жанр по его идентификатору")
    void getExistingById() {
        final Optional<Book> book = repositoryJpa.getById(2);
        assertThat(book)
                .isNotEmpty()
                .matches(b -> b.get().getName().equals("Book2"))
                .matches(b -> b.get().getGenres().size() > 0)
                .matches(b -> b.get().getAuthors().size() > 0);
        final Book actualBook = em.find(Book.class, 2L);
        assertThat(book).isPresent().get()
                .isEqualToComparingFieldByFieldRecursively(actualBook);
    }

    @Test
    @DisplayName(" должен возвращать пустой optional в случае запроса по идентификатору при его отсутствии")
    void getMissingById() {
        final Optional<Book> book = repositoryJpa.getById(6);
        assertThat(book).isEmpty();
    }

    @Test
    @DisplayName(" должен возвращать список книг с корректно заполненными данными ")
    void getAll() {
        final List<Book> books = repositoryJpa.getAll();
        assertThat(books).isNotNull()
                .allMatch(b -> !b.getName().isEmpty())
                .allMatch(b -> b.getGenres().size() > 0)
                .allMatch(b -> b.getAuthors().size() > 0);
    }

    @Test
    @DisplayName(" должен корректно удалять книгу ")
    void delete() {
        final Book book = em.find(Book.class, 1L);
        repositoryJpa.delete(book);
        final Book actualBook = em.find(Book.class, 1L);
        assertThat(actualBook).isNull();
    }

    @Test
    @DisplayName(" должен выкидывать исключение при ппоытке удаления null книги ")
    void deleteNullBook() {
        final Book book = null;
        assertThrows(NullPointerException.class, () -> repositoryJpa.delete(book));
    }

    @Test
    @DisplayName(" должен корректно добавлять книгу по имени с установленным жанром и автором и возвращать его экземпляр со сгенерированным идентификатором")
    void insertCorrect() {
        final String bookName = "Some book";
        final Optional<Genre> genre = genreRepositoryJpa.getById(1);
        final Optional<Author> author = authorRepositoryJpa.getById(1);

        final Book book = repositoryJpa.insert(bookName, author.get(), genre.get().getName());
        assertThat(book)
                .isNotNull()
                .matches(b -> b.getId() > 0)
                .matches(b -> b.getName().equals(bookName))
                .matches(b -> b.getAuthors().contains(author.get()))
                .matches(b -> b.getGenres().contains(genre.get()));
        final Book actualBook = em.find(Book.class, book.getId());
        assertThat(actualBook).isEqualToComparingFieldByFieldRecursively(actualBook);
    }

    @Test
    @DisplayName(" должен вернуть результат с ошибкой если жанр или автор null")
    void insertWithAuthorOrGenreInCorrect() {
        final String bookName = "Some book";
        final Genre genre = null;
        final Author author = null;
        assertThrows(NullPointerException.class, () -> repositoryJpa.insert(bookName, author, null));
    }


}