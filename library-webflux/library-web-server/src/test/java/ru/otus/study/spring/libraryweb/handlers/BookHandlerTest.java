package ru.otus.study.spring.libraryweb.handlers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.domain.Genre;
import ru.otus.study.spring.libraryweb.repository.AuthorRepository;
import ru.otus.study.spring.libraryweb.repository.BookRepository;
import ru.otus.study.spring.libraryweb.repository.GenreRepository;

import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@WebFluxTest(BookHandler.class)
@EnableConfigurationProperties
@AutoConfigureDataMongo
@DisplayName("Обработчик маршрутов работы с книгами должен ")
class BookHandlerTest {
    private final static String TEST_BOOK_NAME = "TestBook";
    private final static String TEST_AUTHOR_NAME = "AuthorName";
    private final static String TEST_GENRE_NAME = "GenreName";
    private final static String TEST_BOOK_ID = "BookId";

    @Mock
    private Book book;
    @Mock
    private Author author;
    @Mock
    private Genre genre;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private BookRepository bookRepository;
    @Autowired
    private ApplicationContext context;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
        given(book.getId()).willReturn(TEST_BOOK_ID);
        given(book.getName()).willReturn(TEST_BOOK_NAME);
        given(book.getAuthorsInfo()).willReturn(TEST_AUTHOR_NAME);
        given(book.getGenresInfo()).willReturn(TEST_GENRE_NAME);
        given(author.getName()).willReturn(TEST_AUTHOR_NAME);
        given(genre.getName()).willReturn(TEST_GENRE_NAME);
    }

    @Test
    @DisplayName(" при запросе списка книг должен передать список книг")
    void shouldReturnAllBooks() {
        given(bookRepository.findAll(any(Sort.class))).willReturn(Flux.just(book));
        webTestClient.get()
                .uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$", hasSize(1)).exists()
                .jsonPath("$[0].id", is(book.getId())).exists()
                .jsonPath("$[0].name", is(book.getName())).exists()
                .jsonPath("$[0].authorsInfo", is(book.getAuthorsInfo())).exists()
                .jsonPath("$[0].genresInfo", is(book.getGenresInfo())).exists();
    }

    @DisplayName("при успешном удалении книги должен возвращать результат NoContent")
    @Test
    void shouldAnswerNoContentOnDelete() {
        given(bookRepository.findById(anyString())).willReturn(Mono.just(book));
        given(bookRepository.delete(any())).willReturn(Mono.empty());
        webTestClient.delete()
                .uri("/api/books/book-id")
                .exchange()
                .expectStatus().isNoContent();
    }

    @DisplayName("при удалении несуществующей книги должен возвращать результат NotFound")
    @Test
    void shouldAnswerNotFoundOnWrongDelete() {
        given(bookRepository.findById(anyString())).willReturn(Mono.empty());
        given(bookRepository.delete(any())).willReturn(Mono.empty());
        webTestClient.delete()
                .uri("/api/books/book-id")
                .exchange()
                .expectStatus().isNotFound();
    }

    @DisplayName("при успешном добавлении книги должен возвращать статус Created и созданную книгу в ответе")
    @Test
    void shouldCreateBookAndReturnCreated() {
        given(bookRepository.save(any())).willReturn(Mono.just(book));
        given(authorRepository.findAllById(any(List.class))).willReturn(Flux.just(author));
        given(genreRepository.findAllById(any(List.class))).willReturn(Flux.just(genre));
        webTestClient.post()
                .uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(String.format("{\"name\":\"%s\",\"authorId\":[\"%s\"],\"genreId\":[\"%s\"]}", TEST_BOOK_NAME, TEST_AUTHOR_NAME, TEST_GENRE_NAME))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id", Matchers.is(book.getId())).exists()
                .jsonPath("$.name", Matchers.is(book.getName())).exists()
                .jsonPath("$.authorsInfo", Matchers.is(book.getAuthorsInfo())).exists()
                .jsonPath("$.genresInfo", Matchers.is(book.getGenresInfo())).exists();
    }

    @DisplayName("при попытке добавления книги без жанров/авторов должен возвращать статус BadRequest")
    @Test
    void shouldReturnErrorIfNoAuthorsOrGenres() {
        webTestClient.post()
                .uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(String.format("{\"name\":\"%s\",\"authorId\":[],\"genreId\":[]}", TEST_BOOK_NAME))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @DisplayName("при попытке добавления книги не найдя жанров/авторов в базе должен возвращать статус NotFound")
    @Test
    void shouldReturnErrorIfNoAuthorsOrGenresFound() {
        given(authorRepository.findAllById(any(List.class))).willReturn(Flux.empty());
        given(genreRepository.findAllById(any(List.class))).willReturn(Flux.empty());
        webTestClient.post()
                .uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(String.format("{\"name\":\"%s\",\"authorId\":[\"23423423\"],\"genreId\":[\"3453\"]}", TEST_BOOK_NAME))
                .exchange()
                .expectStatus().isNotFound();
    }

    @DisplayName("при успешном сохранении книги должен возвращать статус Ok и сохранённую книгу в ответе")
    @Test
    void shouldSaveBookAndReturnSaved() throws Exception {
        given(authorRepository.findAllById(any(List.class))).willReturn(Flux.just(author));
        given(genreRepository.findAllById(any(List.class))).willReturn(Flux.just(genre));
        given(bookRepository.save(any())).willReturn(Mono.just(book));
        given(bookRepository.findById(anyString())).willReturn(Mono.just(book));
        webTestClient.put()
                .uri("/api/books/bookId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(String.format("{\"name\":\"%s\",\"authorId\":[\"%s\"],\"genreId\":[\"%s\"]}", TEST_BOOK_NAME, TEST_AUTHOR_NAME, TEST_GENRE_NAME))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id", Matchers.is(book.getId())).exists()
                .jsonPath("$.name", Matchers.is(book.getName())).exists()
                .jsonPath("$.authorsInfo", Matchers.is(book.getAuthorsInfo())).exists()
                .jsonPath("$.genresInfo", Matchers.is(book.getGenresInfo())).exists();
    }

    @DisplayName("при попытке сохранения несуществующей книги должен возвращать статус NotFound")
    @Test
    void shouldReturnNotFoundWhenSaveNotExistantBook() throws Exception {
        given(bookRepository.findById(anyString())).willReturn(Mono.empty());
        webTestClient.put()
                .uri("/api/books/bookId")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(String.format("{\"name\":\"%s\",\"authorId\":[\"%s\"],\"genreId\":[\"%s\"]}", TEST_BOOK_NAME, TEST_AUTHOR_NAME, TEST_GENRE_NAME))
                .exchange()
                .expectStatus().isNotFound();
    }

    @DisplayName("при попытке сохранения книги без жанров/авторов должен возвращать статус BadRequest")
    @Test
    void shouldReturnBadRequestWhenSaveWithEmptyAuthorsOrGenres() throws Exception {
        given(bookRepository.findById(anyString())).willReturn(Mono.just(book));
        webTestClient.put()
                .uri("/api/books/bookId")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(String.format("{\"name\":\"%s\",\"authorId\":[],\"genreId\":[]}", TEST_BOOK_NAME))
                .exchange()
                .expectStatus().isBadRequest();
    }
}