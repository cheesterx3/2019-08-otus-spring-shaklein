package ru.otus.study.spring.librarydao.repository;

import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.BookComment;

import java.util.List;

public interface BookCommentRepository {
    BookComment commentBook(BookComment bookComment);

    List<BookComment> getBookComments(Book book);
}
