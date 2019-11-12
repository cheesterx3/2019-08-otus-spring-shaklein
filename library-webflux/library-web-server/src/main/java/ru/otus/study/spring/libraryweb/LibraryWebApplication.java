package ru.otus.study.spring.libraryweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import ru.otus.study.spring.libraryweb.handlers.BookHandler;
import ru.otus.study.spring.libraryweb.repository.AuthorRepository;
import ru.otus.study.spring.libraryweb.repository.BookRepository;
import ru.otus.study.spring.libraryweb.repository.GenreRepository;
import ru.otus.study.spring.libraryweb.rest.dto.AuthorDto;
import ru.otus.study.spring.libraryweb.rest.dto.GenreDto;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class LibraryWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryWebApplication.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> bookRoutes(BookHandler bookHandler) {
        return route()
                .GET("/api/books", bookHandler::getAll)
                .DELETE("/api/books/{id}", bookHandler::delete)
                .POST("/api/books", accept(MediaType.APPLICATION_JSON), bookHandler::saveNew)
                .PUT("/api/books/{id}", accept(MediaType.APPLICATION_JSON), bookHandler::save)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> registryRoutes(AuthorRepository authorRepository, GenreRepository genreRepository) {
        return route()
                .GET("/api/authors", serverRequest ->
                        ok().contentType(MediaType.APPLICATION_JSON)
                                .body(authorRepository.findAll(Sort.by(Sort.Order.asc("name"))).map(AuthorDto::toDto), AuthorDto.class))
                .GET("/api/genres", serverRequest ->
                        ok().contentType(MediaType.APPLICATION_JSON)
                                .body(genreRepository.findAll(Sort.by(Sort.Order.asc("name"))).map(GenreDto::toDto), GenreDto.class))
                .build();
    }

}
