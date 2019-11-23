package ru.otus.study.spring.librarymvc.service;


import ru.otus.study.spring.librarymvc.domain.Author;
import ru.otus.study.spring.librarymvc.domain.Book;
import ru.otus.study.spring.librarymvc.exception.DaoException;

import java.util.Optional;

public interface LibraryStorageService {
    Optional<Book> addNewBook(String name, String authorId, String genreName) throws DaoException;

    Optional<Book> addNewBookWithGenreId(String name, String authorId, String genreId) throws DaoException;

    Optional<Author> addNewAuthor(String name) throws DaoException;

    boolean deleteBook(String bookId);

    void updateBook(Book book);

    void deleteGenre(String genreId) throws DaoException;

    void deleteAuthor(String authorId) throws DaoException;

    void removeGenreFromBook(String bookId, String genreId) throws DaoException;

    void addGenreToBook(String bookId, String genreName) throws DaoException;

    void addGenreWithIdToBook(String bookId, String genreId) throws DaoException;

    void removeAuthorFromBook(String bookId, String authorId) throws DaoException;

    void addAuthorToBook(String bookId, String authorId) throws DaoException;

}
