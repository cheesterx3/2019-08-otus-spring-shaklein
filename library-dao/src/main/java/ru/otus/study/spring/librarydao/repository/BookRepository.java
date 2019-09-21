package ru.otus.study.spring.librarydao.repository;

import ru.otus.study.spring.librarydao.model.Author;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.Genre;

import java.util.List;

public interface BookRepository {
    int count();

    Book getById(long id);

    List<Book> getAll();

    boolean deleteById(long id);

    Book insert(String bookName, Author author, Genre genre);
}
