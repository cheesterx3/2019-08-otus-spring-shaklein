package ru.otus.study.spring.libraryspringdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.study.spring.libraryspringdata.domain.Book;
import ru.otus.study.spring.libraryspringdata.domain.BookComment;

import java.util.List;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {
    List<BookComment> getAllByBook(Book book);
}
