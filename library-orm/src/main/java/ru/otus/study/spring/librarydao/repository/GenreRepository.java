package ru.otus.study.spring.librarydao.repository;

import ru.otus.study.spring.librarydao.helper.GenericDaoResult;
import ru.otus.study.spring.librarydao.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {


    Optional<Genre> getById(long id);

    GenericDaoResult<Genre> getByName(String name);


    GenericDaoResult<Genre> insert(String genreName);
}
