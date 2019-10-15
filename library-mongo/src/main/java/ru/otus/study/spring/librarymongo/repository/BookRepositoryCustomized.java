package ru.otus.study.spring.librarymongo.repository;

import ru.otus.study.spring.librarymongo.domain.Author;
import ru.otus.study.spring.librarymongo.domain.Genre;
import ru.otus.study.spring.librarymongo.exception.DaoException;

import java.util.List;

public interface BookRepositoryCustomized {
    void removeGenreFromBookByBookId(String bookId, String genreId) throws DaoException;

    List<Genre> findGenresForBookId(String bookId);

    boolean existsByIdAndGenresContains(String bookId, String genreName);

    void removeAuthorFromBookByBookId(String bookId, String authorId) throws DaoException;

    void addAuthorToBook(String bookId, Author author);

    void addGenreToBook(String bookId, Genre genre);

    boolean hasBookWithSingleAuthorId(String authorId);

    boolean hasBookWithSingleGenreId(String genreId);
}
