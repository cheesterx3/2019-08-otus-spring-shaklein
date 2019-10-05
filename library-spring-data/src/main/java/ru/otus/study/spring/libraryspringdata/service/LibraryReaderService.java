package ru.otus.study.spring.libraryspringdata.service;

import ru.otus.study.spring.libraryspringdata.domain.Book;
import ru.otus.study.spring.libraryspringdata.domain.BookComment;
import ru.otus.study.spring.libraryspringdata.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface LibraryReaderService {
    List<Book> getAllBooks();

    Optional<Book> getBookById(long id);

    List<BookComment> getBookComments(long bookId);

    Optional<BookComment> commentBook(long bookId, String comment) throws DaoException;
}
