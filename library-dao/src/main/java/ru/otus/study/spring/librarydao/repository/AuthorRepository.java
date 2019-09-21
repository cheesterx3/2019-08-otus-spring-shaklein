package ru.otus.study.spring.librarydao.repository;

import ru.otus.study.spring.librarydao.model.Author;

import java.util.List;

public interface AuthorRepository {
    int count();

    Author getById(long id);

    List<Author> getAll();

    boolean deleteById(long id);

    Author insert(String authorName);

}
