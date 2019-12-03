package ru.otus.study.spring.librarymvc.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.study.spring.librarymvc.service.LibraryReaderService;
import ru.otus.study.spring.librarymvc.service.LibraryStorageService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = BookController.class)
@EnableConfigurationProperties
@AutoConfigureDataMongo
@ComponentScan({"ru.otus.study.spring.librarymvc.security"})
@DisplayName("Контроллер работы с книгами ")
class BookControllerSecureTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private LibraryReaderService libraryReaderService;
    @MockBean
    private LibraryStorageService libraryStorageService;

    @WithMockUser(roles = "USER")
    @DisplayName("при переходе в редактирование книги должен выдать ошибку доступа при отсутствии прав на редактирование")
    @Test
    void shouldShowForbiddenOnEditBookPage() throws Exception {
        this.mvc.perform(get("/book/book-id/update"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "USER")
    @DisplayName("при попытке удаления книги, пользователем без прав, должен выдать ошибку доступа")
    @Test
    void shouldShowForbiddenOnBookDelete() throws Exception {
        this.mvc.perform(post("/book/book-id/delete"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "USER")
    @DisplayName("при переходе в создание книги должен выдать ошибку доступа при отсутствии прав на редактирование")
    @Test
    void showShowForbiddenOnAddBookPage() throws Exception {
        this.mvc.perform(get("/book/add"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "USER")
    @DisplayName("при попытке создания книги пользователем без прав должен выдать ошибку доступа")
    @Test
    void shouldShowForbiddenOnBookAdd() throws Exception {
        this.mvc.perform(post("/book/add"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "USER")
    @DisplayName("при попытке применить изменения в книге пользователем без прав должен выдать ошибку доступа")
    @Test
    void shouldShowForbiddenOnEditApply() throws Exception {
        this.mvc.perform(post("/book/update"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "USER")
    @DisplayName("при попытке добавления жанра к книге пользователем без прав должен выдать ошибку доступа")
    @Test
    void shouldRedirectToEditBookPageAfterAddGenre() throws Exception {
        this.mvc.perform(post("/book/book-id/genre").param("genreId", "genre-id"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "USER")
    @DisplayName("при попытке добавления автора к книге пользователем без прав должен выдать ошибку доступа")
    @Test
    void shouldRedirectToEditBookPageAfterAddAuthor() throws Exception {
        this.mvc.perform(post("/book/book-id/author").param("authorId", "author-id"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "USER")
    @DisplayName("при попытке удаления автора у книги пользователем без прав должен выдать ошибку доступа")
    @Test
    void shouldRedirectToEditBookPageAfterDeleteAuthor() throws Exception {
        this.mvc.perform(post("/book/book-id/author/author-id/delete"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "USER")
    @DisplayName("при попытке удаления жанра у книги пользователем без прав должен выдать ошибку доступа")
    @Test
    void shouldRedirectToEditBookPageAfterDeleteGenre() throws Exception {
        this.mvc.perform(post("/book/book-id/genre/genre-id/delete"))
                .andExpect(status().isForbidden());
    }

}