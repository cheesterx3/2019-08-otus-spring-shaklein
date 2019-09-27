package ru.otus.study.spring.librarydao.repository;

import ru.otus.study.spring.librarydao.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    int count();

    Optional<Genre> getById(long id);

    List<Genre> getAll();

    boolean deleteById(long id);

    Genre insert(String genreName);
}
