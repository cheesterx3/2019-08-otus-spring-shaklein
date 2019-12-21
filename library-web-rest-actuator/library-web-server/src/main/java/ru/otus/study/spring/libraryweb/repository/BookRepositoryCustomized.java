package ru.otus.study.spring.libraryweb.repository;

import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Genre;
import ru.otus.study.spring.libraryweb.exception.DaoException;
import ru.otus.study.spring.libraryweb.exception.NotFoundException;

import java.util.List;
import java.util.Map;

public interface BookRepositoryCustomized {
    void removeGenreFromBookByBookId(String bookId, String genreId) throws DaoException, NotFoundException;

    List<Genre> findGenresForBookId(String bookId);

    boolean existsByIdAndGenresContains(String bookId, String genreName);

    boolean existsByIdAndGenresContainsById(String bookId, String genreId);

    void removeAuthorFromBookByBookId(String bookId, String authorId) throws DaoException;

    void addAuthorToBook(String bookId, Author author);

    void addGenreToBook(String bookId, Genre genre);

    boolean hasBookWithSingleAuthorId(String authorId);

    boolean hasBookWithSingleGenreId(String genreId);

    Map<Genre, Long> countBooksByGenre();

    Map<Author, Long> countBooksByAuthor();
}
