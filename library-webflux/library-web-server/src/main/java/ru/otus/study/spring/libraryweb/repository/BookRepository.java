package ru.otus.study.spring.libraryweb.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.study.spring.libraryweb.domain.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

}
