package ru.otus.study.spring.librarymongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.study.spring.librarymongo.domain.BookComment;

import java.util.List;

public interface BookCommentRepository extends MongoRepository<BookComment, String>,BookCommentRepositoryCustomized{
    List<BookComment> findAllByBook_Id(String bookId);
}
