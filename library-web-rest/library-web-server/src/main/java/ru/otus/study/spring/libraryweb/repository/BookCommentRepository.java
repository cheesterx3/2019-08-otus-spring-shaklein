package ru.otus.study.spring.libraryweb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.study.spring.libraryweb.domain.BookComment;

import java.util.List;

public interface BookCommentRepository extends MongoRepository<BookComment, String>,BookCommentRepositoryCustomized{
    List<BookComment> findAllByBook_Id(String bookId);
}
