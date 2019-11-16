package ru.otus.study.spring.libraryweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.domain.Genre;
import ru.otus.study.spring.libraryweb.exception.DaoException;
import ru.otus.study.spring.libraryweb.exception.NotFoundException;
import ru.otus.study.spring.libraryweb.repository.AuthorRepository;
import ru.otus.study.spring.libraryweb.repository.GenreRepository;
import ru.otus.study.spring.libraryweb.rest.dto.BookDtoForSave;

import java.util.List;

import static reactor.core.publisher.Mono.zip;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    public Mono<Book> createBookForSave(Book savedBook, Mono<BookDtoForSave> dtoForSaveMono) {
        return checkAuthorsAndGenresAndReturnBook(dtoForSaveMono)
                .flatMap(book -> collectAuthorsAndGenres(book)
                        .flatMap(authorsAndGenresList -> createBook(savedBook, book, authorsAndGenresList)));
    }

    @Override
    public Mono<Book> createNewBookFromDto(Mono<BookDtoForSave> dtoForSaveMono) {
        return checkAuthorsAndGenresAndReturnBook(dtoForSaveMono)
                .flatMap(bookDtoForSave -> collectAuthorsAndGenres(bookDtoForSave)
                        .flatMap(authorsAndGenresList -> Mono.just(new Book())
                                .flatMap(book -> createBook(book, bookDtoForSave, authorsAndGenresList))));
    }

    private Mono<BookDtoForSave> checkAuthorsAndGenresAndReturnBook(Mono<BookDtoForSave> dtoForSaveMono) {
        return dtoForSaveMono
                .filter(bookDtoForSave -> !bookDtoForSave.getGenreId().isEmpty())
                .switchIfEmpty(Mono.error(new DaoException("Genres cannot be empty")))
                .filter(bookDtoForSave -> !bookDtoForSave.getAuthorId().isEmpty())
                .switchIfEmpty(Mono.error(new DaoException("Authors cannot be empty")));
    }

    private Mono<Tuple2<List<Author>, List<Genre>>> collectAuthorsAndGenres(BookDtoForSave book) {
        return zip(authorRepository.findAllById(book.getAuthorId()).collectList(),
                genreRepository.findAllById(book.getGenreId()).collectList())
                .filter(authorsAndGenresList -> !authorsAndGenresList.getT1().isEmpty())
                .switchIfEmpty(Mono.error(new NotFoundException("Authors not found")))
                .filter(authorsAndGenresList -> !authorsAndGenresList.getT2().isEmpty())
                .switchIfEmpty(Mono.error(new NotFoundException("Genres not found")));
    }

    private Mono<? extends Book> createBook(Book savedBook, BookDtoForSave book, Tuple2<List<Author>, List<Genre>> objects) {
        savedBook.setName(book.getName());
        savedBook.setAuthors(objects.getT1());
        savedBook.setGenres(objects.getT2());
        return Mono.just(savedBook);
    }
}
