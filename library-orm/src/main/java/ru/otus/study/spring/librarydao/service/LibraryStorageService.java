package ru.otus.study.spring.librarydao.service;

import ru.otus.study.spring.librarydao.helper.GenericDaoResult;
import ru.otus.study.spring.librarydao.model.Book;

public interface LibraryStorageService {
    GenericDaoResult<Book> addNewBook(String name, long authorId, String genreName);

    boolean deleteBook(long bookId);
}
