package ru.otus.study.spring.librarydao.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.study.spring.librarydao.helper.GenericDaoResult;
import ru.otus.study.spring.librarydao.model.Author;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.Genre;
import ru.otus.study.spring.librarydao.repository.AuthorRepository;
import ru.otus.study.spring.librarydao.repository.BookRepository;
import ru.otus.study.spring.librarydao.repository.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = LibraryStorageServiceImpl.class)
@DisplayName(" сервис управления книгами в библиотеке должен ")
class LibraryStorageServiceImplTest {
    private final static String ADDITION_ERROR = "book add error";
    private final static String AUTHOR_NOT_FOUND_ERROR = "Author not found";
    private final static String BOOK_NAME="TestBook";
    private final static String GENRE_NAME="TestGenre";

    private final static long EXISTING_AUTHOR_ID=1L;
    private final static long MISSING_AUTHOR_ID=2L;

    @Mock
    private Author testAuthor;
    @Mock
    private Book testBook;

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @Autowired
    private LibraryStorageService libraryStorageService;

    @BeforeEach
    void setUp() {
        given(authorRepository.getById(EXISTING_AUTHOR_ID)).willReturn(Optional.of(testAuthor));
        given(authorRepository.getById(MISSING_AUTHOR_ID)).willReturn(Optional.empty());
        given(bookRepository.insert(anyString(), any(), nullable(String.class))).willReturn(GenericDaoResult.createError(ADDITION_ERROR));
        given(bookRepository.insert(anyString(), any(), anyString())).willReturn(GenericDaoResult.createResult(testBook));
        given(bookRepository.deleteById(anyLong())).willReturn(true);
    }

    @Test
    @DisplayName(" корректно добавлять книгу в библиотеку при вводе верных данных")
    void addNewBookCorrect() {
        final GenericDaoResult<Book> bookGenericDaoResult = libraryStorageService.addNewBook(BOOK_NAME, EXISTING_AUTHOR_ID, GENRE_NAME);
        assertThat(bookGenericDaoResult.getResult())
                .isPresent()
                .get().isEqualTo(testBook);
    }

    @Test
    @DisplayName(" возвращать ошибку об отстутвии автора добавления книги  в библиотеку при вводе несуществующего автора")
    void addNewBookAuthorInCorrect() {

        final GenericDaoResult<Book> bookGenericDaoResult = libraryStorageService.addNewBook(BOOK_NAME, MISSING_AUTHOR_ID, GENRE_NAME);
        assertThat(bookGenericDaoResult.getResult())
                .isNotPresent();
        assertThat(bookGenericDaoResult.getError()).isNotEmpty().isEqualTo(AUTHOR_NOT_FOUND_ERROR);
    }

    @Test
    @DisplayName(" возвращать ошибку  добавления книги в библиотеку при вводе некорректного жанра")
    void addNewBookGenreInCorrect() {
        final GenericDaoResult<Book> bookGenericDaoResult = libraryStorageService.addNewBook(BOOK_NAME, EXISTING_AUTHOR_ID, null);
        assertThat(bookGenericDaoResult.getResult())
                .isNotPresent();
        assertThat(bookGenericDaoResult.getError()).isNotEmpty().isEqualTo(ADDITION_ERROR);
    }

    @Test
    @DisplayName(" корректно удалять книгу из библиотеки")
    void deleteBook() {
        final boolean deleteBook = libraryStorageService.deleteBook(1);
        assertTrue(deleteBook);
    }
}