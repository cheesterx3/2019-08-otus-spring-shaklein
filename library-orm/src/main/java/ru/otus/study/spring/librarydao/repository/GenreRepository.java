package ru.otus.study.spring.librarydao.repository;

import ru.otus.study.spring.librarydao.model.Genre;

import java.util.Optional;

public interface GenreRepository {


    Optional<Genre> getById(long id);

    Optional<Genre> getByName(String name);


    Genre insert(String genreName);
}
