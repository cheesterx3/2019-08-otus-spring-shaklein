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
import reactor.util.function.Tuple2;
import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.domain.Genre;
import ru.otus.study.spring.libraryweb.exception.DaoException;
import ru.otus.study.spring.libraryweb.exception.NotFoundException;
import ru.otus.study.spring.libraryweb.repository.AuthorRepository;
import ru.otus.study.spring.libraryweb.repository.BookRepository;
import ru.otus.study.spring.libraryweb.repository.GenreRepository;
import ru.otus.study.spring.libraryweb.rest.dto.BookDtoForSave;
import ru.otus.study.spring.libraryweb.rest.dto.BookDtoSimple;

import java.net.URI;
import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static reactor.core.publisher.Mono.zip;

@Component
@RequiredArgsConstructor
public class BookHandler {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @NonNull
    public Mono<ServerResponse> saveNew(ServerRequest request) {
        final Mono<BookDtoForSave> dtoForSaveMono = request.bodyToMono(BookDtoForSave.class);

        return checkAuthorsAndGenresAndReturnBook(dtoForSaveMono)
                .flatMap(bookDtoForSave -> collectAuthorsAndGenres(bookDtoForSave)
                        .flatMap(authorsAndGenresList -> Mono.just(new Book())
                                .flatMap(book -> createBook(book, bookDtoForSave, authorsAndGenresList))))
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
                .flatMap(savedBook -> checkAuthorsAndGenresAndReturnBook(dtoForSaveMono)
                        .flatMap(book -> collectAuthorsAndGenres(book)
                                .flatMap(authorsAndGenresList -> createBook(savedBook, book, authorsAndGenresList)))
                        .flatMap(book -> ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(bookRepository.save(book).map(BookDtoSimple::toDto), BookDtoSimple.class)))
                .switchIfEmpty(Mono.error(new NotFoundException("Book not found")))
                .onErrorResume(DaoException.class,
                        e -> Mono.just(e.getMessage()).flatMap(s -> badRequest().contentType(MediaType.TEXT_PLAIN).bodyValue(s)))
                .onErrorResume(NotFoundException.class,
                        e -> Mono.just(e.getMessage()).flatMap(s -> status(HttpStatus.NOT_FOUND).contentType(MediaType.TEXT_PLAIN).bodyValue(s)));
    }

    private Mono<BookDtoForSave> checkAuthorsAndGenresAndReturnBook(Mono<BookDtoForSave> dtoForSaveMono) {
        return dtoForSaveMono
                .filter(bookDtoForSave -> !bookDtoForSave.getGenreId().isEmpty())
                .switchIfEmpty(Mono.error(new DaoException("Genres cannot be empty")))
                .filter(bookDtoForSave -> !bookDtoForSave.getAuthorId().isEmpty())
                .switchIfEmpty(Mono.error(new DaoException("Authors cannot be empty")));
    }

    private Mono<? extends Book> createBook(Book savedBook, BookDtoForSave book, Tuple2<List<Author>, List<Genre>> objects) {
        savedBook.setName(book.getName());
        savedBook.setAuthors(objects.getT1());
        savedBook.setGenres(objects.getT2());
        return Mono.just(savedBook);
    }

    private Mono<Tuple2<List<Author>, List<Genre>>> collectAuthorsAndGenres(BookDtoForSave book) {
        return zip(authorRepository.findAllById(book.getAuthorId()).collectList(),
                genreRepository.findAllById(book.getGenreId()).collectList())
                .filter(authorsAndGenresList -> !authorsAndGenresList.getT1().isEmpty())
                .switchIfEmpty(Mono.error(new NotFoundException("Authors not found")))
                .filter(authorsAndGenresList -> !authorsAndGenresList.getT2().isEmpty())
                .switchIfEmpty(Mono.error(new NotFoundException("Genres not found")));
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
