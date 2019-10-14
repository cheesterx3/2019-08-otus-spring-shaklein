package ru.otus.study.spring.librarymongo.service;


import ru.otus.study.spring.librarymongo.domain.Author;
import ru.otus.study.spring.librarymongo.domain.Book;
import ru.otus.study.spring.librarymongo.exception.DaoException;

import java.util.Optional;

public interface LibraryStorageService {
    Optional<Book> addNewBook(String name, String authorId, String genreName) throws DaoException;

    Optional<Author> addNewAuthor(String name) throws DaoException;

    boolean deleteBook(String bookId);

    void deleteGenre(String genreId) throws DaoException;

    void deleteAuthor(String authorId) throws DaoException;

    void removeGenreFromBook(String bookId, String genreId) throws DaoException;

    void addGenreToBook(String bookId, String genreName) throws DaoException;

    void removeAuthorFromBook(String bookId, String authorId) throws DaoException;

    void addAuthorToBook(String bookId,String authorId) throws DaoException;

}
