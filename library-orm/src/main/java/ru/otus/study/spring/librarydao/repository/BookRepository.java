package ru.otus.study.spring.librarydao.repository;

import ru.otus.study.spring.librarydao.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Optional<Book> getById(long id);

    List<Book> getAll();

    void delete(Book book);

    Book insert(Book book);


}
