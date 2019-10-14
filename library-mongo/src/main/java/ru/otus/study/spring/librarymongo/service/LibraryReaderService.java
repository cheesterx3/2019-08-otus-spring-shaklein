package ru.otus.study.spring.librarymongo.service;


import ru.otus.study.spring.librarymongo.domain.Author;
import ru.otus.study.spring.librarymongo.domain.Book;
import ru.otus.study.spring.librarymongo.domain.BookComment;
import ru.otus.study.spring.librarymongo.domain.Genre;
import ru.otus.study.spring.librarymongo.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface LibraryReaderService {
    List<Author> findAllAuthors();

    List<Genre> findAllGenres();

    List<Book> getAllBooksSortedByName();

    List<Book> getBooksByNameLike(String name);

    List<BookComment> getBookComments(String bookId);

    Optional<BookComment> commentBook(String bookId, String comment) throws DaoException;
}
