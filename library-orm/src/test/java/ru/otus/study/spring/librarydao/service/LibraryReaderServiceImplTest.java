package ru.otus.study.spring.librarydao.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.study.spring.librarydao.helper.GenericDaoResult;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.BookComment;
import ru.otus.study.spring.librarydao.repository.BookCommentRepository;
import ru.otus.study.spring.librarydao.repository.BookRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = LibraryReaderServiceImpl.class)
@DisplayName(" сервис работы с книгами из библиотеки должен ")
class LibraryReaderServiceImplTest {
    private final static long MISSING_BOOK_ID=10L;

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
        given(bookRepository.getAll()).willReturn(Collections.singletonList(testBook));
        given(bookRepository.getById(anyLong())).willReturn(Optional.of(testBook));
        given(bookCommentRepository.getBookComments(any())).willReturn(Collections.singletonList(testComment));
        given(bookCommentRepository.commentBook(any(),anyString())).willReturn(testComment);
        given(bookRepository.getById(eq(MISSING_BOOK_ID))).willReturn(Optional.empty());
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
    void commentBook() {
        final GenericDaoResult<BookComment> bookComment = libraryReaderService.commentBook(1, "some comment");
        assertThat(bookComment.getResult()).isPresent().get().isEqualTo(testComment);
    }

    @Test
    @DisplayName(" возвращать ошибку при попытке добавить комментарий к несуществующей книге ")
    void commentMissingBook() {
        final GenericDaoResult<BookComment> bookComment = libraryReaderService.commentBook(MISSING_BOOK_ID, "some comment");
        assertThat(bookComment.getResult()).isNotPresent();
        assertThat(bookComment.getError()).isNotEmpty();
    }
}