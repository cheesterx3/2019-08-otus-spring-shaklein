package ru.otus.study.spring.librarymvc.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.study.spring.librarymvc.domain.Author;
import ru.otus.study.spring.librarymvc.domain.Book;
import ru.otus.study.spring.librarymvc.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Репозиторий работы с книгами с учётом безопасности должен ")
@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.study.spring.librarymvc.config",
        "ru.otus.study.spring.librarymvc.repository",
        "ru.otus.study.spring.librarymvc.events",
        "ru.otus.study.spring.librarymvc.security"})
class BookRepositorySecurityTest {

    private static final int EXPECTED_ADMIN_BOOK_COUNT = 4;
    private static final int EXPECTED_USER_BOOK_COUNT = 3;
    @Autowired
    private BookRepository bookRepository;

    @DisplayName("выдёргивать из БД корректное кол-во книг для администратора")
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetBookListAccordingPermissionsForAdmin() {
        final List<Book> allBooks = bookRepository.findAll();
        assertThat(allBooks.size()).isEqualTo(EXPECTED_ADMIN_BOOK_COUNT);
    }

    @DisplayName("выдёргивать из БД корректное кол-во книг для пользователя")
    @Test
    @WithMockUser(roles = "USER")
    void shouldGetBookListAccordingPermissionsForUser() {
        final List<Book> allBooks = bookRepository.findAll();
        assertThat(allBooks.size()).isEqualTo(EXPECTED_USER_BOOK_COUNT);
    }

    @DisplayName("при удалении книги пользователем без прав выдать ошибку")
    @Test
    @WithMockUser(roles = "USER")
    void shouldThrowAccessDeniedOnUserBookDeleteAttempt() {
        final List<Book> allBooks = bookRepository.findAll();
        final Book book = allBooks.get(0);
        assertThrows(AccessDeniedException.class, () -> bookRepository.delete(book));
    }

    @DisplayName("при попытке изменения жанров в книге пользователем без прав выдать ошибку")
    @Test
    @WithMockUser(roles = "USER")
    void shouldThrowAccessDeniedOnUserBookGenreUpdateAttempt() {
        final List<Book> allBooks = bookRepository.findAll();
        final Book book = allBooks.get(0);
        final Genre genre = book.getGenres().get(0);
        assertThrows(AccessDeniedException.class, () -> bookRepository.removeGenreFromBookByBookId(book.getId(), genre.getId()));
        assertThrows(AccessDeniedException.class, () -> bookRepository.addGenreToBook(book.getId(), genre));
    }

    @DisplayName("при попытке изменения авторов в книге пользователем без прав выдать ошибку")
    @Test
    @WithMockUser(roles = "USER")
    void shouldThrowAccessDeniedOnUserBookAuthorUpdateAttempt() {
        final List<Book> allBooks = bookRepository.findAll();
        final Book book = allBooks.get(0);
        final Author author = book.getAuthors().get(0);
        assertThrows(AccessDeniedException.class, () -> bookRepository.removeAuthorFromBookByBookId(book.getId(), author.getId()));
        assertThrows(AccessDeniedException.class, () -> bookRepository.addAuthorToBook(book.getId(), author));
    }

}