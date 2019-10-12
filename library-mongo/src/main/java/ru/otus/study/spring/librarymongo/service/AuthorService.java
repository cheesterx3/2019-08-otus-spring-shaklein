package ru.otus.study.spring.librarymongo.service;

import ru.otus.study.spring.librarymongo.domain.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAllAuthors();
}
