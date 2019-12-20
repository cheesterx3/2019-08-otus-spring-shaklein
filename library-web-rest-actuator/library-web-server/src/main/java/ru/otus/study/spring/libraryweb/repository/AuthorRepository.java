package ru.otus.study.spring.libraryweb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.study.spring.libraryweb.domain.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {
    boolean existsById(String id);

    boolean existsByNameEqualsIgnoreCase(String name);
}
