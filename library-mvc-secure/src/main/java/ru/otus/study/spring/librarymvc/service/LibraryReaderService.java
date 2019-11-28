package ru.otus.study.spring.librarymvc.service;


import ru.otus.study.spring.librarymvc.domain.*;
import ru.otus.study.spring.librarymvc.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface LibraryReaderService {
    List<Author> findAllAuthors();

    List<Genre> findAllGenres();

    List<Book> getAllBooksSortedByName();

    List<Book> getBooksByNameLike(String name);

    Optional<Book> findBookById(String bookId);

    List<BookComment> getBookComments(String bookId);

    Optional<BookComment> commentBook(String bookId, String comment, User user) throws NotFoundException;
}
