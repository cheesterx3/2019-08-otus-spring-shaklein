package ru.otus.study.spring.libraryspringdata.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.study.spring.libraryspringdata.domain.Book;
import ru.otus.study.spring.libraryspringdata.domain.BookComment;
import ru.otus.study.spring.libraryspringdata.exception.DaoException;
import ru.otus.study.spring.libraryspringdata.repository.BookCommentRepository;
import ru.otus.study.spring.libraryspringdata.repository.BookRepository;

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
    private final static long MISSING_BOOK_ID = 10L;

    @Mock
    private Book testBook;
    @Mock
    private BookComment testComment;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private BookCommentRepository bookCommentRepository;
    @Autowired
    private LibraryReaderService libraryReaderService;

    @BeforeEach
    void setUp() {
        given(bookRepository.findAll()).willReturn(Collections.singletonList(testBook));
        given(bookRepository.findById(anyLong())).willReturn(Optional.of(testBook));
        given(bookCommentRepository.getAllByBook(any())).willReturn(Collections.singletonList(testComment));
        given(bookCommentRepository.save(any())).willReturn(testComment);
        given(bookRepository.findById(eq(MISSING_BOOK_ID))).willReturn(Optional.empty());
    }

    @Test
    @DisplayName(" корректно возвращать список книг из библиотеки ")
    void testAllBooksReceive() {
        final List<Book> allBooks = libraryReaderService.getAllBooks();
        assertThat(allBooks).hasSize(1).contains(testBook);
    }

    @Test
    @DisplayName(" корректно возвращать книгу из библиотеки по её идентификатору ")
    void getBookById() {
        final Optional<Book> book = libraryReaderService.getBookById(1);
        assertThat(book).isPresent().get().isEqualTo(testBook);
    }

    @Test
    @DisplayName(" корректно возвращать список комментариев к книге по указанному идентификатору ")
    void getBookComments() {
        final List<BookComment> bookComments = libraryReaderService.getBookComments(1);
        assertThat(bookComments).hasSize(1).contains(testComment);
    }

    @Test
    @DisplayName(" корректно добавлять новый комментарий к книге ")
    void commentBook() throws DaoException {
        final Optional<BookComment> bookComment = libraryReaderService.commentBook(1, "some comment");
        assertThat(bookComment).isPresent().get().isEqualTo(testComment);
    }

    @Test
    @DisplayName(" выкидывать exception при попытке добавить комментарий к несуществующей книге ")
    void commentMissingBook() {
        assertThrows(DaoException.class, () -> libraryReaderService.commentBook(MISSING_BOOK_ID, "some comment"));
    }
}