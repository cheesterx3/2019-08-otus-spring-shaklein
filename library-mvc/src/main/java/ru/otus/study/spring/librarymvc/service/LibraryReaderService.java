package ru.otus.study.spring.librarymvc.service;


import ru.otus.study.spring.librarymvc.domain.Author;
import ru.otus.study.spring.librarymvc.domain.Book;
import ru.otus.study.spring.librarymvc.domain.BookComment;
import ru.otus.study.spring.librarymvc.domain.Genre;
import ru.otus.study.spring.librarymvc.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface LibraryReaderService {
    List<Author> findAllAuthors();

    List<Genre> findAllGenres();

    List<Book> getAllBooksSortedByName();

    List<Book> getBooksByNameLike(String name);

    Optional<Book> findBookById(String bookId);

    List<BookComment> getBookComments(String bookId);

    Optional<BookComment> commentBook(String bookId, String comment) throws DaoException;
}
