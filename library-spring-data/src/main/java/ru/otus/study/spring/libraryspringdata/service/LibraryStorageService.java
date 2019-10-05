package ru.otus.study.spring.libraryspringdata.service;

import ru.otus.study.spring.libraryspringdata.domain.Book;
import ru.otus.study.spring.libraryspringdata.exception.DaoException;

import java.util.Optional;

public interface LibraryStorageService {
    Optional<Book> addNewBook(String name, long authorId, String genreName) throws DaoException;

    boolean deleteBook(long bookId);
}
