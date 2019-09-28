package ru.otus.study.spring.librarydao.repository;

import ru.otus.study.spring.librarydao.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {

    Optional<Author> getById(long id);

    List<Author> getAll();

    boolean deleteById(long authorId);

    Author insert(String authorName);

}
