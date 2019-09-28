package ru.otus.study.spring.librarydao.repository;

import ru.otus.study.spring.librarydao.helper.GenericDaoResult;
import ru.otus.study.spring.librarydao.model.Author;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.BookComment;
import ru.otus.study.spring.librarydao.model.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Optional<Book> getById(long id);

    List<Book> getAll();

    boolean deleteById(long id);

    GenericDaoResult<Book> insert(String bookName, Author author, String genre);

    BookComment commentBook(Book book, String comment);
}
