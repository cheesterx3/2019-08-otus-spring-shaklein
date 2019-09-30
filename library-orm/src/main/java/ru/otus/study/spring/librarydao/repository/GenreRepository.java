package ru.otus.study.spring.librarydao.repository;

import ru.otus.study.spring.librarydao.helper.GenericDaoResult;
import ru.otus.study.spring.librarydao.model.Genre;

import java.util.Optional;

public interface GenreRepository {


    Optional<Genre> getById(long id);

    GenericDaoResult<Genre> getByName(String name);


    Genre insert(String genreName);
}
