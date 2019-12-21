package ru.otus.study.spring.libraryweb.service;


import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.exception.DaoException;
import ru.otus.study.spring.libraryweb.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface LibraryStorageService {
    Optional<Book> addNewBook(String name, String authorId, String genreName) throws DaoException, NotFoundException;

    Optional<Book> addNewBookWithGenreId(String name, String authorId, String genreId) throws DaoException, NotFoundException;

    Optional<Book> addNewBook(String name, List<String> authorId, List<String> genreId) throws DaoException;

    Optional<Book> saveBook(String bookId, String name, List<String> authorId, List<String> genreId) throws DaoException, NotFoundException;

    Optional<Author> addNewAuthor(String name) throws DaoException;

    boolean deleteBook(String bookId);

    void updateBook(Book book);

    boolean isBookExistsById(String bookId);

    boolean isAuthorExistsById(String authorId);

    boolean isGenreExistsById(String genreId);

    void deleteGenre(String genreId) throws DaoException;

    void deleteAuthor(String authorId) throws DaoException;

    void removeGenreFromBook(String bookId, String genreId) throws DaoException, NotFoundException;

    void addGenreToBook(String bookId, String genreName) throws DaoException, NotFoundException;

    void addGenreWithIdToBook(String bookId, String genreId) throws DaoException, NotFoundException;

    void removeAuthorFromBook(String bookId, String authorId) throws DaoException, NotFoundException;

    void addAuthorToBook(String bookId, String authorId) throws DaoException, NotFoundException;

}
