package ru.otus.study.spring.librarydao.repository;

import ru.otus.study.spring.librarydao.model.Author;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    int count();

    Optional<Book> getById(long id);

    List<Book> getAll();

    boolean deleteById(long id);

    Book insert(String bookName, Author author, Genre genre);
}
