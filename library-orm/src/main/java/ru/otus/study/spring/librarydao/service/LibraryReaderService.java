package ru.otus.study.spring.librarydao.service;

import ru.otus.study.spring.librarydao.helper.GenericDaoResult;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.BookComment;

import java.util.List;
import java.util.Optional;

public interface LibraryReaderService {
    List<Book> getAllBooks();

    Optional<Book> getBookById(long id);

    List<BookComment> getBookComments(long bookId);

    GenericDaoResult<BookComment> commentBook(long bookId, String comment);
}
