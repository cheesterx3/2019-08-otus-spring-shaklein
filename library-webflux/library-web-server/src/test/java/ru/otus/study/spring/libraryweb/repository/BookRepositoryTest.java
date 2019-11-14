package ru.otus.study.spring.libraryweb.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.domain.Genre;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static reactor.core.publisher.Mono.zip;

@DataMongoTest
@EnableConfigurationProperties
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Репозиторий книг ")
class BookRepositoryTest {
    private final static String TEST_BOOK_NAME = "TestBook";
    private final static String TEST_AUTHOR_NAME = "AuthorName";
    private final static String TEST_GENRE_NAME = "GenreName";

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("должен возвращать список книг из репозитория ")
    void shouldReturnAllBooks(){
        final Mono<Genre> genreMono = genreRepository.save(new Genre(TEST_GENRE_NAME));
        final Mono<Author> authorMono = authorRepository.save(new Author(TEST_AUTHOR_NAME));

        final Flux<Book> all = bookRepository.findAll();

        StepVerifier
                .create(zip(genreMono,authorMono)
                        .flatMap(authorGenrePair->bookRepository.save(new Book(TEST_BOOK_NAME,
                                Collections.singletonList(authorGenrePair.getT2()),
                                Collections.singletonList(authorGenrePair.getT1()))))
                .thenMany(all))
                .consumeNextWith(book ->
                {
                    assertThat(book.getName()).isEqualTo(TEST_BOOK_NAME);
                    assertThat(book.getGenres().size()).isEqualTo(1);
                    assertThat(book.getAuthors().size()).isEqualTo(1);
                })
                .verifyComplete();
    }
}