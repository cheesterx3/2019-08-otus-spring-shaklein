package ru.otus.study.spring.librarydao.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.BookComment;
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


    @Mock
    private Book testBook;
    @Mock
    private BookComment testComment;
    @MockBean
    private BookRepository bookRepository;
    @Autowired
    private LibraryReaderService libraryReaderService;

    @BeforeEach
    void setUp() {
        given(bookRepository.getAll()).willReturn(Collections.singletonList(testBook));
        given(bookRepository.getById(anyLong())).willReturn(Optional.of(testBook));
        given(testBook.getComments()).willReturn(Collections.singletonList(testComment));
        given(bookRepository.commentBook(any(),anyString())).willReturn(testComment);
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
        final BookComment bookComment = libraryReaderService.commentBook(1, "some comment");
        assertThat(bookComment).isNotNull().isEqualTo(testComment);
    }
}