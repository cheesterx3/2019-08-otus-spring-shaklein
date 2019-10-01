package ru.otus.study.spring.librarydao.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.study.spring.librarydao.exception.DaoException;
import ru.otus.study.spring.librarydao.model.Author;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.Genre;
import ru.otus.study.spring.librarydao.repository.AuthorRepository;
import ru.otus.study.spring.librarydao.repository.BookRepository;
import ru.otus.study.spring.librarydao.repository.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = LibraryStorageServiceImpl.class)
@DisplayName(" сервис управления книгами в библиотеке должен ")
class LibraryStorageServiceImplTest {
    private final static String GENRE_CANNOT_BE_NULL = "Genre cannot be null";
    private final static String AUTHOR_NOT_FOUND_ERROR = "Author not found";
    private final static String BOOK_NAME = "TestBook";
    private final static String GENRE_NAME = "TestGenre";
    private final static String BOOK_INFO = "TestBookInfo";

    private final static long EXISTING_AUTHOR_ID = 1L;
    private final static long MISSING_AUTHOR_ID = 2L;

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
        given(authorRepository.getById(EXISTING_AUTHOR_ID)).willReturn(Optional.of(testAuthor));
        given(authorRepository.getById(MISSING_AUTHOR_ID)).willReturn(Optional.empty());
        given(genreRepository.getByName(anyString())).willReturn(Optional.of(testGenre));
        given(bookRepository.insert(any())).willReturn(testBook);
        given(bookRepository.getById(anyLong())).willReturn(Optional.of(testBook));
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
        assertThrows(DaoException.class,()->libraryStorageService.addNewBook(BOOK_NAME, MISSING_AUTHOR_ID, GENRE_NAME));
    }

    @Test
    @DisplayName(" выкидывать исключение  добавления книги в библиотеку при вводе некорректного жанра")
    void addNewBookGenreInCorrect() {
        assertThrows(DaoException.class,()->libraryStorageService.addNewBook(BOOK_NAME, EXISTING_AUTHOR_ID, null));
    }

    @Test
    @DisplayName(" корректно удалять книгу из библиотеки")
    void deleteBook() {
        final boolean deleteBook = libraryStorageService.deleteBook(1);
        assertTrue(deleteBook);
    }
}