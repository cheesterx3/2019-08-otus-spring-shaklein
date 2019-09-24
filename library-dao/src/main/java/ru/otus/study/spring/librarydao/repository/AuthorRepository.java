package ru.otus.study.spring.librarydao.repository;

import ru.otus.study.spring.librarydao.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    int count();

    Optional<Author> getById(long id);

    List<Author> getAll();

    boolean deleteById(long id);

    Author insert(String authorName);

}
