package ru.otus.study.spring.libraryweb.service;

import reactor.core.publisher.Mono;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.rest.dto.BookDtoForSave;

public interface BookService {
    Mono<Book> createBookForSave(Book savedBook, Mono<BookDtoForSave> dtoForSaveMono);

    Mono<Book> createNewBookFromDto(Mono<BookDtoForSave> bookDtoForSave);
}
