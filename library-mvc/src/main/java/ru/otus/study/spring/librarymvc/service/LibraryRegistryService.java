package ru.otus.study.spring.librarymvc.service;

import ru.otus.study.spring.librarymvc.domain.Author;
import ru.otus.study.spring.librarymvc.domain.Genre;
import ru.otus.study.spring.librarymvc.exception.DaoException;

import java.util.Optional;

public interface LibraryRegistryService {
    Optional<Genre> addNewGenre(String genreName);

    void removeGenre(String genreId) throws DaoException;

    Optional<Author> addAuthor(String authorName);

    void removeAuthor(String authorId) throws DaoException;
}
