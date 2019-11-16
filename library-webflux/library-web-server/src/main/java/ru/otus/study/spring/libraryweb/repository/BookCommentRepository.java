package ru.otus.study.spring.libraryweb.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.study.spring.libraryweb.domain.BookComment;

public interface BookCommentRepository extends ReactiveMongoRepository<BookComment, String>{
}
