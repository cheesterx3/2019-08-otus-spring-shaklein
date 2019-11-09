package ru.otus.study.spring.libraryweb.service;


import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.domain.BookComment;
import ru.otus.study.spring.libraryweb.domain.Genre;
import ru.otus.study.spring.libraryweb.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface LibraryReaderService {
    List<Author> findAllAuthors();

    List<Genre> findAllGenres();

    List<Book> getAllBooksSortedByName();

    List<Book> getBooksByNameLike(String name);

    Optional<Book> findBookById(String bookId);

    List<BookComment> getBookComments(String bookId);

    Optional<BookComment> commentBook(String bookId, String comment) throws NotFoundException;
}
