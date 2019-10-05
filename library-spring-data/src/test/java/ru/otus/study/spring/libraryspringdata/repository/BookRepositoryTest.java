package ru.otus.study.spring.libraryspringdata.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.study.spring.libraryspringdata.domain.Author;
import ru.otus.study.spring.libraryspringdata.domain.Book;
import ru.otus.study.spring.libraryspringdata.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий работы с книгами ")
@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName(" должен возвращать жанр по его идентификатору")
    void getExistingById() {
        final Optional<Book> book = bookRepository.findById(2L);
        assertThat(book)
                .isNotEmpty()
                .get()
                .matches(b -> b.getName().equals("Book2"))
                .matches(b -> !b.getGenres().isEmpty())
                .matches(b -> !b.getAuthors().isEmpty());
        final Book actualBook = em.find(Book.class, 2L);
        assertThat(book).isPresent().get()
                .isEqualToComparingFieldByFieldRecursively(actualBook);
    }

    @Test
    @DisplayName(" должен возвращать пустой optional в случае запроса по идентификатору при его отсутствии")
    void getMissingById() {
        final Optional<Book> book = bookRepository.findById(6L);
        assertThat(book).isEmpty();
    }

    @Test
    @DisplayName(" должен возвращать список книг с корректно заполненными данными ")
    void getAll() {
        final List<Book> books = bookRepository.findAll();
        assertThat(books).isNotNull()
                .allMatch(b -> !b.getName().isEmpty())
                .allMatch(b -> !b.getGenres().isEmpty())
                .allMatch(b -> !b.getAuthors().isEmpty());
    }

    @Test
    @DisplayName(" должен корректно удалять книгу ")
    void delete() {
        final Book book = em.find(Book.class, 1L);
        bookRepository.delete(book);
        final Book actualBook = em.find(Book.class, 1L);
        assertThat(actualBook).isNull();
    }


    @Test
    @DisplayName(" должен корректно добавлять книгу по имени с установленным жанром и автором и возвращать его экземпляр со сгенерированным идентификатором")
    void insertCorrect() {
        final String bookName = "Some book";
        final Genre genre = em.find(Genre.class, 1L);
        final Author author = em.find(Author.class, 1L);
        final Book book = new Book(bookName, author, genre);
        final Book expectedBook = bookRepository.save(book);
        assertThat(expectedBook)
                .isNotNull()
                .matches(b -> b.getId() > 0)
                .matches(b -> b.getName().equals(bookName))
                .matches(b -> b.getAuthors().contains(author))
                .matches(b -> b.getGenres().contains(genre));
        final Book actualBook = em.find(Book.class, expectedBook.getId());
        assertThat(actualBook).isEqualToComparingFieldByFieldRecursively(actualBook);
    }


}