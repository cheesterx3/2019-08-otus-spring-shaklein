package ru.otus.study.spring.librarymongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.study.spring.librarymongo.domain.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findAllByNameLike(String name);
}
