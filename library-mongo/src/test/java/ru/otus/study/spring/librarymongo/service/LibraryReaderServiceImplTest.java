package ru.otus.study.spring.librarymongo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import ru.otus.study.spring.librarymongo.domain.Book;
import ru.otus.study.spring.librarymongo.domain.BookComment;
import ru.otus.study.spring.librarymongo.exception.DaoException;
import ru.otus.study.spring.librarymongo.repository.AuthorRepository;
import ru.otus.study.spring.librarymongo.repository.BookCommentRepository;
import ru.otus.study.spring.librarymongo.repository.BookRepository;
import ru.otus.study.spring.librarymongo.repository.GenreRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = LibraryReaderServiceImpl.class)
@DisplayName(" сервис работы с книгами из библиотеки должен ")
class LibraryReaderServiceImplTest {
    private final static String MISSING_BOOK_ID = "some_unusual_ident";

    @Mock
    private Book testBook;
    @Mock
    private BookComment testComment;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private BookCommentRepository bookCommentRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @Autowired
    private LibraryReaderService libraryReaderService;


    @BeforeEach
    void setUp() {
        given(bookRepository.findAll(any(Sort.class))).willReturn(Collections.singletonList(testBook));
        given(bookRepository.findById(anyString())).willReturn(Optional.of(testBook));
        given(bookCommentRepository.findAllByBook_Id(any())).willReturn(Collections.singletonList(testComment));
        given(bookCommentRepository.save(any())).willReturn(testComment);
        given(bookRepository.findById(eq(MISSING_BOOK_ID))).willReturn(Optional.empty());
        given(bookRepository.findAllByNameLike(anyString())).willReturn(Collections.singletonList(testBook));
    }

    @Test
    @DisplayName(" корректно возвращать список книг из библиотеки ")
    void testAllBooksReceive() {
        final List<Book> allBooks = libraryReaderService.getAllBooksSortedByName();
        assertThat(allBooks).hasSize(1).contains(testBook);
    }

    @Test
    @DisplayName(" корректно возвращать список книг по совпадению по имени ")
    void getBookById() {
        final List<Book> books = libraryReaderService.getBooksByNameLike("н");
        assertThat(books).isNotEmpty().contains(testBook);
    }

    @Test
    @DisplayName(" корректно возвращать список комментариев к книге по указанному идентификатору ")
    void getBookComments() {
        final List<BookComment> bookComments = libraryReaderService.getBookComments("some_code");
        assertThat(bookComments).hasSize(1).contains(testComment);
    }

    @Test
    @DisplayName(" корректно добавлять новый комментарий к книге ")
    void commentBook() throws DaoException {
        final Optional<BookComment> bookComment = libraryReaderService.commentBook("some_code", "some comment");
        assertThat(bookComment).isPresent().get().isEqualTo(testComment);
    }

    @Test
    @DisplayName(" выкидывать exception при попытке добавить комментарий к несуществующей книге ")
    void commentMissingBook() {
        assertThrows(DaoException.class, () -> libraryReaderService.commentBook(MISSING_BOOK_ID, "some comment"));
    }
}