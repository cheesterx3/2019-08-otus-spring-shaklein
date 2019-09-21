package ru.otus.study.spring.librarydao.repository;

import ru.otus.study.spring.librarydao.model.Genre;

import java.util.List;

public interface GenreRepository {
    int count();

    Genre getById(long id);

    List<Genre> getAll();

    boolean deleteById(long id);

    Genre insert(String genreName);
}
