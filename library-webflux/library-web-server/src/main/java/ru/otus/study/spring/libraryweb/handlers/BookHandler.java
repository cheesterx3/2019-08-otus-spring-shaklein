package ru.otus.study.spring.libraryweb.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.exception.DaoException;
import ru.otus.study.spring.libraryweb.exception.NotFoundException;
import ru.otus.study.spring.libraryweb.repository.BookRepository;
import ru.otus.study.spring.libraryweb.rest.dto.BookDtoForSave;
import ru.otus.study.spring.libraryweb.rest.dto.BookDtoSimple;
import ru.otus.study.spring.libraryweb.service.BookService;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static reactor.core.publisher.Mono.zip;

@Component
@RequiredArgsConstructor
public class BookHandler {
    private final BookRepository bookRepository;
    private final BookService bookService;

    @NonNull
    public Mono<ServerResponse> saveNew(ServerRequest request) {
        final Mono<BookDtoForSave> dtoForSaveMono = request.bodyToMono(BookDtoForSave.class);

        return bookService.createNewBookFromDto(dtoForSaveMono)
                .flatMap(book -> created(getBookAddAUri(book))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(bookRepository.save(book).map(BookDtoSimple::toDto), BookDtoSimple.class))
                .onErrorResume(DaoException.class,
                        e -> Mono.just(e.getMessage()).flatMap(s -> badRequest().contentType(MediaType.TEXT_PLAIN).bodyValue(s)))
                .onErrorResume(NotFoundException.class,
                        e -> Mono.just(e.getMessage()).flatMap(s -> status(HttpStatus.NOT_FOUND).contentType(MediaType.TEXT_PLAIN).bodyValue(s)));
    }

    @NonNull
    public Mono<ServerResponse> save(ServerRequest request) {
        final Mono<BookDtoForSave> dtoForSaveMono = request.bodyToMono(BookDtoForSave.class);

        return bookRepository.findById(request.pathVariable("id"))
                .flatMap(savedBook -> bookService.createBookForSave(savedBook,dtoForSaveMono)
                        .flatMap(book -> ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(bookRepository.save(book).map(BookDtoSimple::toDto), BookDtoSimple.class)))
                .switchIfEmpty(Mono.error(new NotFoundException("Book not found")))
                .onErrorResume(DaoException.class,
                        e -> Mono.just(e.getMessage()).flatMap(s -> badRequest().contentType(MediaType.TEXT_PLAIN).bodyValue(s)))
                .onErrorResume(NotFoundException.class,
                        e -> Mono.just(e.getMessage()).flatMap(s -> status(HttpStatus.NOT_FOUND).contentType(MediaType.TEXT_PLAIN).bodyValue(s)));
    }

    @NonNull
    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookRepository.findAll(Sort.by(Sort.Order.asc("name")))
                        .map(BookDtoSimple::toDto), BookDtoSimple.class);
    }

    @NonNull
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return bookRepository.findById(serverRequest.pathVariable("id"))
                .flatMap(book -> noContent().build(bookRepository.delete(book)))
                .switchIfEmpty(status(HttpStatus.NOT_FOUND)
                        .bodyValue(String.format("Book with id [%s] not found", serverRequest.pathVariable("id"))));
    }

    private URI getBookAddAUri(Book book) {
        return UriComponentsBuilder.fromPath("/api/books/" + book.getId()).build().toUri();
    }
}
