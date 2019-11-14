package ru.otus.study.spring.libraryweb.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.domain.Genre;
import ru.otus.study.spring.libraryweb.exception.DaoException;
import ru.otus.study.spring.libraryweb.exception.NotFoundException;
import ru.otus.study.spring.libraryweb.repository.AuthorRepository;
import ru.otus.study.spring.libraryweb.repository.GenreRepository;
import ru.otus.study.spring.libraryweb.rest.dto.BookDtoForSave;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest
@EnableConfigurationProperties
@AutoConfigureDataMongo
@DisplayName("Сервис работы с книгами должен ")
class BookServiceImplTest {
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
    @Autowired
    private BookService bookService;

    @BeforeEach
    void setUp() {
        given(book.getId()).willReturn(TEST_BOOK_ID);
        given(book.getName()).willReturn(TEST_BOOK_NAME);
        given(book.getAuthorsInfo()).willReturn(TEST_AUTHOR_NAME);
        given(book.getGenresInfo()).willReturn(TEST_GENRE_NAME);
        given(author.getName()).willReturn(TEST_AUTHOR_NAME);
        given(genre.getName()).willReturn(TEST_GENRE_NAME);
    }

    @Test
    @DisplayName("при сохранении возвращать Mono с обновлённой книгой ")
    void createBookForSaveSuccess() {
        final String newBookName = "NewBookName";
        final BookDtoForSave dtoForSave = new BookDtoForSave(newBookName, Collections.singletonList("TestId"), Collections.singletonList("TestId"));
        final Book bookToUpdate = new Book();
        given(authorRepository.findAllById(any(List.class))).willReturn(Flux.just(author));
        given(genreRepository.findAllById(any(List.class))).willReturn(Flux.just(genre));
        StepVerifier
                .create(bookService.createBookForSave(bookToUpdate, Mono.just(dtoForSave)))
                .assertNext(bookToCheck -> {
                    assertThat(bookToCheck.getName()).isEqualTo(newBookName);
                    assertThat(bookToCheck.getAuthors()).containsExactly(author);
                    assertThat(bookToCheck.getGenres()).containsExactly(genre);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("при сохранении возвращать Mono с NotFoundException в случае если авторы/жанры не найдены ")
    void createBookForSaveNotFound() {
        final BookDtoForSave dtoForSave = new BookDtoForSave("NewBookName", Collections.singletonList("TestId"), Collections.singletonList("TestId"));
        given(authorRepository.findAllById(any(List.class))).willReturn(Flux.empty());
        given(genreRepository.findAllById(any(List.class))).willReturn(Flux.just(genre));
        StepVerifier
                .create(bookService.createBookForSave(book, Mono.just(dtoForSave)))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("при сохранении возвращать Mono с DaoException в случае если пытаются сохранить книгу без авторов/жанров ")
    void createBookForSaveDaoError() {
        final BookDtoForSave dtoForSave = mock(BookDtoForSave.class);
        given(dtoForSave.getAuthorId()).willReturn(Collections.emptyList());
        given(dtoForSave.getGenreId()).willReturn(Collections.emptyList());
        StepVerifier
                .create(bookService.createBookForSave(book, Mono.just(dtoForSave)))
                .expectError(DaoException.class)
                .verify();
    }

    @Test
    @DisplayName("при создании новой книги возвращать Mono с книгой ")
    void createNewBookFromDto() {
        final String newBookName = "NewBookName";
        final BookDtoForSave dtoForSave = new BookDtoForSave(newBookName, Collections.singletonList("TestId"), Collections.singletonList("TestId"));
        given(authorRepository.findAllById(any(List.class))).willReturn(Flux.just(author));
        given(genreRepository.findAllById(any(List.class))).willReturn(Flux.just(genre));
        StepVerifier
                .create(bookService.createNewBookFromDto(Mono.just(dtoForSave)))
                .assertNext(bookToCheck -> {
                    assertThat(bookToCheck.getName()).isEqualTo(newBookName);
                    assertThat(bookToCheck.getAuthors()).containsExactly(author);
                    assertThat(bookToCheck.getGenres()).containsExactly(genre);
                })
                .verifyComplete();
    }


    @Test
    @DisplayName("при создании новой книги возвращать Mono с NotFoundException в случае если авторы/жанры не найдены ")
    void createNewBookFromDtoNotFound() {
        final BookDtoForSave dtoForSave = new BookDtoForSave("NewBookName", Collections.singletonList("TestId"), Collections.singletonList("TestId"));
        given(authorRepository.findAllById(any(List.class))).willReturn(Flux.empty());
        given(genreRepository.findAllById(any(List.class))).willReturn(Flux.just(genre));
        StepVerifier
                .create(bookService.createNewBookFromDto(Mono.just(dtoForSave)))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("при создании новой книги возвращать Mono с DaoException в случае если пытаются сохранить книгу без авторов/жанров ")
    void createNewBookFromDtoDaoError() {
        final BookDtoForSave dtoForSave = mock(BookDtoForSave.class);
        given(dtoForSave.getAuthorId()).willReturn(Collections.emptyList());
        given(dtoForSave.getGenreId()).willReturn(Collections.emptyList());
        StepVerifier
                .create(bookService.createNewBookFromDto(Mono.just(dtoForSave)))
                .expectError(DaoException.class)
                .verify();
    }
}