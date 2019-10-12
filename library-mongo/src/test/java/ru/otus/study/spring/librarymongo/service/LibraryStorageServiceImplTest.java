package ru.otus.study.spring.librarymongo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.study.spring.librarymongo.domain.Author;
import ru.otus.study.spring.librarymongo.domain.Book;
import ru.otus.study.spring.librarymongo.domain.Genre;
import ru.otus.study.spring.librarymongo.exception.DaoException;
import ru.otus.study.spring.librarymongo.repository.AuthorRepository;
import ru.otus.study.spring.librarymongo.repository.BookRepository;
import ru.otus.study.spring.librarymongo.repository.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = LibraryStorageServiceImpl.class)
@DisplayName(" сервис управления книгами в библиотеке должен ")
class LibraryStorageServiceImplTest {

    private final static String BOOK_NAME = "TestBook";
    private final static String GENRE_NAME = "TestGenre";

    private final static String EXISTING_AUTHOR_ID = "EXISTING_ID";
    private final static String MISSING_AUTHOR_ID = "MISSING_ID";

    @Mock
    private Author testAuthor;
    @Mock
    private Book testBook;
    @Mock
    private Genre testGenre;

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @Autowired
    private LibraryStorageService libraryStorageService;

    @BeforeEach
    void setUp() {
        given(authorRepository.findById(eq(EXISTING_AUTHOR_ID))).willReturn(Optional.of(testAuthor));
        given(authorRepository.findById(eq(MISSING_AUTHOR_ID))).willReturn(Optional.empty());
        given(genreRepository.findByNameIgnoreCase(anyString())).willReturn(Optional.of(testGenre));
        given(bookRepository.save(any())).willReturn(testBook);
        given(bookRepository.findById(anyString())).willReturn(Optional.of(testBook));
    }

    @Test
    @DisplayName(" корректно добавлять книгу в библиотеку при вводе верных данных")
    void addNewBookCorrect() throws DaoException {
        final Optional<Book> bookGenericDaoResult = libraryStorageService.addNewBook(BOOK_NAME, EXISTING_AUTHOR_ID, GENRE_NAME);
        assertThat(bookGenericDaoResult)
                .isPresent()
                .get()
                .isEqualTo(testBook);
    }

    @Test
    @DisplayName(" выкидывать исключение об отстутвии автора добавления книги  в библиотеку при вводе несуществующего автора")
    void addNewBookAuthorInCorrect() {
        assertThrows(DaoException.class, () -> libraryStorageService.addNewBook(BOOK_NAME, MISSING_AUTHOR_ID, GENRE_NAME));
    }

    @Test
    @DisplayName(" выкидывать исключение  добавления книги в библиотеку при вводе некорректного жанра")
    void addNewBookGenreInCorrect() {
        assertThrows(DaoException.class, () -> libraryStorageService.addNewBook(BOOK_NAME, EXISTING_AUTHOR_ID, null));
    }

    @Test
    @DisplayName(" корректно удалять книгу из библиотеки")
    void deleteBook() {
        final boolean deleteBook = libraryStorageService.deleteBook("ANY_ID");
        assertTrue(deleteBook);
    }
}