package ru.otus.study.spring.librarymvc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.study.spring.librarymvc.domain.Book;
import ru.otus.study.spring.librarymvc.service.LibraryReaderService;
import ru.otus.study.spring.librarymvc.service.LibraryStorageService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BookController.class)
@AutoConfigureMockMvc
class BookControllerTest {
    private final static String TEST_BOOK_NAME = "TestBook";
    private final static String TEST_AUTHOR_NAME = "AuthorName";
    private final static String TEST_GENRE_NAME = "GenreName";

    @Autowired
    private MockMvc mvc;

    @Mock
    private Book book;
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

    @Description(" при запросе списка книг должен передать во view с именем books модель со списком книг")
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

    @Description("при переходе в редактирование должен передать во view с именем editBook модель с книгой и списками авторов и жанров")
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
                .andExpect(model().attribute("book",book))
                .andDo(print())
                .andReturn();
    }
}