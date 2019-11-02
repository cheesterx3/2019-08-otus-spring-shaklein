package ru.otus.study.spring.libraryweb.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.domain.Genre;
import ru.otus.study.spring.libraryweb.exception.DaoException;
import ru.otus.study.spring.libraryweb.exception.NotFoundException;
import ru.otus.study.spring.libraryweb.service.LibraryReaderService;
import ru.otus.study.spring.libraryweb.service.LibraryStorageService;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = BookController.class)
@EnableConfigurationProperties
@AutoConfigureDataMongo
@DisplayName("Контроллер работы с книгами должен")
class BookControllerTest {
    private final static String TEST_BOOK_NAME = "TestBook";
    private final static String TEST_AUTHOR_NAME = "AuthorName";
    private final static String TEST_GENRE_NAME = "GenreName";
    private final static String TEST_BOOK_ID = "BookId";

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
        given(book.getId()).willReturn(TEST_BOOK_ID);
        given(book.getName()).willReturn(TEST_BOOK_NAME);
        given(book.getAuthorsInfo()).willReturn(TEST_AUTHOR_NAME);
        given(book.getGenresInfo()).willReturn(TEST_GENRE_NAME);
    }

    @DisplayName(" при запросе списка книг должен передать список книг")
    @Test
    void shouldShowBookList() throws Exception {
        given(libraryReaderService.getAllBooksSortedByName()).willReturn(Collections.singletonList(book));
        this.mvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(book.getId())))
                .andExpect(jsonPath("$[0].name", is(book.getName())))
                .andExpect(jsonPath("$[0].authorsInfo", is(book.getAuthorsInfo())))
                .andExpect(jsonPath("$[0].genresInfo", is(book.getGenresInfo())))
                .andDo(print())
                .andReturn();
    }

    @DisplayName("при успешном удалении книги должен возвращать результат NoContent")
    @Test
    void shouldAnswerNoContentOnDelete() throws Exception {
        given(libraryStorageService.deleteBook(anyString())).willReturn(true);
        this.mvc.perform(delete("/api/books/book-id"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("при удалении несуществующей книги должен возвращать результат NotFound")
    @Test
    void shouldAnswerNotFoundOnWrongDelete() throws Exception {
        given(libraryStorageService.deleteBook(anyString())).willReturn(false);
        this.mvc.perform(delete("/api/books/book-id"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("при успешном добавлении книги должен возвращать статус Created и созданную книгу в ответе")
    @Test
    void shouldCreateBookAndReturnCreated() throws Exception {
        given(libraryStorageService.addNewBook(anyString(), anyList(), anyList())).willReturn(Optional.of(book));
        this.mvc.perform(
                post("/api/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"name\":\"%s\",\"authorId\":[\"%s\"],\"genreId\":[\"%s\"]}", TEST_BOOK_NAME, TEST_AUTHOR_NAME, TEST_GENRE_NAME)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(book.getId())))
                .andExpect(jsonPath("$.name", is(book.getName())))
                .andExpect(jsonPath("$.authorsInfo", is(book.getAuthorsInfo())))
                .andExpect(jsonPath("$.genresInfo", is(book.getGenresInfo())))
                .andReturn();
    }

    @DisplayName("при попытке добавления книги без жанров/авторов должен возвращать статус BadRequest с текстом ошибки в ответе")
    @Test
    void shouldReturnErrorIfNoAuthorsOrGenres() throws Exception {
        final String errorMessage = "Authors are empty";
        given(libraryStorageService.addNewBook(anyString(), anyList(), anyList())).willThrow(new DaoException(errorMessage));
        this.mvc.perform(
                post("/api/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"name\":\"%s\",\"authorId\":[],\"genreId\":[]}", TEST_BOOK_NAME)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage))
                .andReturn();
    }

    @DisplayName("при успешном сохранении книги должен возвращать статус Ok и сохранённую книгу в ответе")
    @Test
    void shouldSaveBookAndReturnSaved() throws Exception {
        given(libraryStorageService.saveBook(anyString(), anyString(), anyList(), anyList())).willReturn(Optional.of(book));
        this.mvc.perform(
                put("/api/books/bookId")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"name\":\"%s\",\"authorId\":[\"%s\"],\"genreId\":[\"%s\"]}", TEST_BOOK_NAME, TEST_AUTHOR_NAME, TEST_GENRE_NAME)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(book.getId())))
                .andExpect(jsonPath("$.name", is(book.getName())))
                .andExpect(jsonPath("$.authorsInfo", is(book.getAuthorsInfo())))
                .andExpect(jsonPath("$.genresInfo", is(book.getGenresInfo())))
                .andReturn();
    }

    @DisplayName("при попытке сохранения несуществующей книги должен возвращать статус NotFound с текстом ошибки в ответе")
    @Test
    void shouldReturnNotFoundWhenSaveNotExistantBook() throws Exception {
        final String errorMessage = "Book not found";
        given(libraryStorageService.saveBook(anyString(), anyString(), anyList(), anyList())).willThrow(new NotFoundException(errorMessage));
        this.mvc.perform(
                put("/api/books/bookId")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"name\":\"%s\",\"authorId\":[\"%s\"],\"genreId\":[\"%s\"]}", TEST_BOOK_NAME, TEST_AUTHOR_NAME, TEST_GENRE_NAME)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage))
                .andReturn();
    }

    @DisplayName("при попытке сохранения книги без жанров/авторов должен возвращать статус BadRequest с текстом ошибки в ответе")
    @Test
    void shouldReturnBadRequestWhenSaveWithEmptyAuthorsOrGenres() throws Exception {
        final String errorMessage = "Authors cannot be empty";
        given(libraryStorageService.saveBook(anyString(), anyString(), anyList(), anyList())).willThrow(new DaoException(errorMessage));
        this.mvc.perform(
                put("/api/books/bookId")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"name\":\"%s\",\"authorId\":[],\"genreId\":[]}", TEST_BOOK_NAME)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage))
                .andReturn();
    }

}