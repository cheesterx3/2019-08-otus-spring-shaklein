package ru.otus.study.spring.librarydao.service;

import ru.otus.study.spring.librarydao.exception.DaoException;
import ru.otus.study.spring.librarydao.model.Book;

import java.util.Optional;

public interface LibraryStorageService {
    Optional<Book> addNewBook(String name, long authorId, String genreName) throws DaoException;

    boolean deleteBook(long bookId);
}
