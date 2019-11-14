package ru.otus.study.spring.libraryweb.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.study.spring.libraryweb.repository.AuthorRepository;
import ru.otus.study.spring.libraryweb.repository.GenreRepository;
import ru.otus.study.spring.libraryweb.rest.dto.AuthorDto;
import ru.otus.study.spring.libraryweb.rest.dto.GenreDto;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class RegistryHandler {
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;


    @NonNull
    public Mono<ServerResponse> getAuthors(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(authorRepository.findAll(Sort.by(Sort.Order.asc("name")))
                        .map(AuthorDto::toDto), AuthorDto.class);
    }

    @NonNull
    public Mono<ServerResponse> getGenres(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                        .body(genreRepository.findAll(Sort.by(Sort.Order.asc("name"))).map(GenreDto::toDto), GenreDto.class);
    }
}
