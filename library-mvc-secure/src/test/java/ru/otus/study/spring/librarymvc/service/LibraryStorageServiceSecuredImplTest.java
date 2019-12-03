package ru.otus.study.spring.librarymvc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.study.spring.librarymvc.domain.Author;
import ru.otus.study.spring.librarymvc.domain.Book;
import ru.otus.study.spring.librarymvc.domain.Genre;
import ru.otus.study.spring.librarymvc.exception.DaoException;
import ru.otus.study.spring.librarymvc.repository.AuthorRepository;
import ru.otus.study.spring.librarymvc.repository.BookRepository;
import ru.otus.study.spring.librarymvc.repository.GenreRepository;
import ru.otus.study.spring.librarymvc.securityacl.dao.AclRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.study.spring.librarymvc.config",
        "ru.otus.study.spring.librarymvc.repository",
        "ru.otus.study.spring.librarymvc.events",
        "ru.otus.study.spring.librarymvc.securityacl.dao"})
@DisplayName(" сервис управления книгами в библиотеке должен ")
class LibraryStorageServiceSecuredImplTest {

    private final static String BOOK_NAME = "TestBook";

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AclRepository aclRepository;
    @Autowired
    private LibraryStorageService libraryStorageService;

    @Test
    @WithMockUser(username = "admin")
    @DisplayName(" добавить данные по доступу для книги после её добавления")
    void addAclOnNewBook() throws DaoException {
        final Author author = authorRepository.findAll().get(0);
        final Genre genre = genreRepository.findAll().get(0);
        final Optional<Book> bookGenericDaoResult = libraryStorageService.addNewBook(BOOK_NAME, author.getId(), genre.getId());
        assertThat(bookGenericDaoResult)
                .isPresent()
                .map(book -> new ObjectIdentityImpl(Book.class, book.getId()))
                .map(objectIdentity -> aclRepository.findById(objectIdentity.getIdentifier()))
                .isPresent();
    }

}