package ru.otus.study.spring.libraryweb.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.service.LibraryReaderService;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(value = AuthorController.class)
@EnableConfigurationProperties
@AutoConfigureDataMongo
@DisplayName("Контроллер работы с авторами должен")
class AuthorControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private LibraryReaderService libraryReaderService;

    @Test
    @DisplayName(" возвращать список всех доступных авторов")
    void getAll() throws Exception {
        given(libraryReaderService.findAllAuthors())
                .willReturn(Arrays.asList(
                        new Author("Автор 1"),
                        new Author("Автор 2"),
                        new Author("Автор 3")));
        this.mvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Автор 1")))
                .andExpect(jsonPath("$[1].name", is("Автор 2")))
                .andExpect(jsonPath("$[2].name", is("Автор 3")))
                .andDo(print())
                .andReturn();
    }
}