package ru.otus.study.spring.librarymongo.service;

import org.springframework.stereotype.Service;
import ru.otus.study.spring.librarymongo.domain.Author;
import ru.otus.study.spring.librarymongo.domain.Genre;
import ru.otus.study.spring.librarymongo.exception.DaoException;

import java.util.Optional;

@Service
public class LibraryRegistryServiceImpl implements LibraryRegistryService {
    @Override
    public Optional<Genre> addNewGenre(String genreName) {
        return Optional.empty();
    }

    @Override
    public void removeGenre(String genreId) throws DaoException {

    }

    @Override
    public Optional<Author> addAuthor(String authorName) {
        return Optional.empty();
    }

    @Override
    public void removeAuthor(String authorId) throws DaoException {

    }
}
