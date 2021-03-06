package ru.otus.study.spring.librarymvc.controller;

import org.hamcrest.BaseMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.CollectionUtils;
import ru.otus.study.spring.librarymvc.domain.Author;
import ru.otus.study.spring.librarymvc.domain.Book;
import ru.otus.study.spring.librarymvc.domain.Genre;
import ru.otus.study.spring.librarymvc.service.LibraryReaderService;
import ru.otus.study.spring.librarymvc.service.LibraryStorageService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BookController.class)
@AutoConfigureMockMvc
@DisplayName("Контроллер работы с книгами должен")
class BookControllerTest {
    private final static String TEST_BOOK_NAME = "TestBook";
    private final static String TEST_AUTHOR_NAME = "AuthorName";
    private final static String TEST_GENRE_NAME = "GenreName";

    @Autowired
    private MockMvc mvc;

    @Mock
    private Book book;
    @Mock
    private Author author;
    @Mock
    private Genre genre;
    @MockBean
    private LibraryReaderService libraryReaderService;
    @MockBean
    private LibraryStorageService libraryStorageService;

    @BeforeEach
    void setUp() {
        given(book.getName()).willReturn(TEST_BOOK_NAME);
        given(book.getAuthorsInfo()).willReturn(TEST_AUTHOR_NAME);
        given(book.getGenresInfo()).willReturn(TEST_GENRE_NAME);
    }

    @DisplayName(" при запросе списка книг должен передать во view с именем books модель со списком книг")
    @Test
    void shouldShowBookList() throws Exception {
        given(libraryReaderService.getAllBooksSortedByName()).willReturn(Collections.singletonList(book));
        final List<Book> expectedModel = libraryReaderService.getAllBooksSortedByName();
        this.mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", expectedModel))
                .andDo(print())
                .andReturn();
    }

    @DisplayName("при переходе в редактирование должен передать во view с именем editBook модель с книгой и списками авторов и жанров")
    @Test
    void shouldShowEditBookPage() throws Exception {
        given(libraryReaderService.findBookById(anyString())).willReturn(Optional.of(book));
        final List<Book> expectedModel = libraryReaderService.getAllBooksSortedByName();
        this.mvc.perform(get("/book/book-id/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("editBook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("filteredAuthors"))
                .andExpect(model().attributeExists("filteredGenres"))
                .andExpect(model().attribute("book", book))
                .andDo(print())
                .andReturn();
    }

    @DisplayName("при успешном удалении книги должен редиректить на страницу со списком книг")
    @Test
    void shouldRedirectToListOnBookDelete() throws Exception {
        this.mvc.perform(post("/book/book-id/delete"))
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("отображать страничку добавления новой книги")
    @Test
    void showShowAddBookPage() throws Exception {
        given(libraryReaderService.findAllAuthors()).willReturn(Collections.singletonList(author));
        given(libraryReaderService.findAllGenres()).willReturn(Collections.singletonList(genre));
        this.mvc.perform(get("/book/add"))
                .andExpect(view().name("addBook"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("bookForm"));
    }

    @DisplayName("при успешном добавлении книги должен редиректить на страницу со списком книг")
    @Test
    void shouldRedirectToListOnBookAdd() throws Exception {
        this.mvc.perform(post("/book/add"))
                .andExpect(redirectedUrl("/book/add"));
    }

    @DisplayName("должен открывать страницу ошибки 404 при редактирвоании несуществующей книги")
    @Test
    void shouldShowErrorPageOnIncorrectBookEditPage() throws Exception {
        this.mvc.perform(get("/book/book-id/update"))
                .andExpect(view().name("err404"));
    }

    @DisplayName("должен открывать страницу редактирования книги")
    @Test
    void shouldShowBookEditPage() throws Exception {
        given(libraryReaderService.findBookById(anyString())).willReturn(Optional.of(book));
        this.mvc.perform(get("/book/book-id/update"))
                .andExpect(view().name("editBook"));
    }

    @DisplayName("должен возвращаться на страницу редактирования книги после успеха редактирования")
    @Test
    void shouldRedirectToEditBookPageAfterEditApply() throws Exception {
        this.mvc.perform(post("/book/update").param("id","book-id"))
                .andExpect(redirectedUrl("/book/book-id/update"));
    }

    @DisplayName("должен возвращаться на страницу редактирования книги после добавления жанра к книге")
    @Test
    void shouldRedirectToEditBookPageAfterAddGenre() throws Exception {
        this.mvc.perform(post("/book/book-id/genre").param("genreId","genre-id"))
                .andExpect(redirectedUrl("/book/book-id/update"));
    }

    @DisplayName("должен возвращаться на страницу редактирования книги после добавления автора к книге")
    @Test
    void shouldRedirectToEditBookPageAfterAddAuthor() throws Exception {
        this.mvc.perform(post("/book/book-id/author").param("authorId","author-id"))
                .andExpect(redirectedUrl("/book/book-id/update"));
    }

    @DisplayName("должен возвращаться на страницу редактирования книги после удаления автора у книги")
    @Test
    void shouldRedirectToEditBookPageAfterDeleteAuthor() throws Exception {
        this.mvc.perform(post("/book/book-id/author/author-id/delete"))
                .andExpect(redirectedUrl("/book/book-id/update"));
    }

    @DisplayName("должен возвращаться на страницу редактирования книги после удаления жанра у книги")
    @Test
    void shouldRedirectToEditBookPageAfterDeleteGenre() throws Exception {
        this.mvc.perform(post("/book/book-id/genre/genre-id/delete"))
                .andExpect(redirectedUrl("/book/book-id/update"));
    }
}