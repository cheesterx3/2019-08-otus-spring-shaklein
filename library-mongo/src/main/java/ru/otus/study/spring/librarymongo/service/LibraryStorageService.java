package ru.otus.study.spring.librarymongo.service;


import ru.otus.study.spring.librarymongo.domain.Book;
import ru.otus.study.spring.librarymongo.exception.DaoException;

import java.util.Optional;

public interface LibraryStorageService {
    Optional<Book> addNewBook(String name, String authorId, String genreName) throws DaoException;

    boolean deleteBook(String bookId);
}
