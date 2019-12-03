package ru.otus.study.spring.librarymvc.repository;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.study.spring.librarymvc.domain.Author;
import ru.otus.study.spring.librarymvc.domain.Genre;
import ru.otus.study.spring.librarymvc.exception.DaoException;

import java.util.List;

public interface BookRepositoryCustomized {
    @PreAuthorize(value = "hasPermission(#bookId,'ru.otus.study.spring.librarymvc.domain.Book','ADMINISTRATION')")
    void removeGenreFromBookByBookId(String bookId, String genreId) throws DaoException;

    List<Genre> findGenresForBookId(String bookId);

    boolean existsByIdAndGenresContains(String bookId, String genreName);

    boolean existsByIdAndGenresContainsById(String bookId, String genreId);

    @PreAuthorize(value = "hasPermission(#bookId,'ru.otus.study.spring.librarymvc.domain.Book','ADMINISTRATION')")
    void removeAuthorFromBookByBookId(String bookId, String authorId) throws DaoException;

    @PreAuthorize(value = "hasPermission(#bookId,'ru.otus.study.spring.librarymvc.domain.Book','ADMINISTRATION')")
    void addAuthorToBook(String bookId, Author author);

    @PreAuthorize(value = "hasPermission(#bookId,'ru.otus.study.spring.librarymvc.domain.Book','ADMINISTRATION')")
    void addGenreToBook(String bookId, Genre genre);

    boolean hasBookWithSingleAuthorId(String authorId);

    boolean hasBookWithSingleGenreId(String genreId);
}
