package ru.otus.study.spring.librarymvc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.study.spring.librarymvc.domain.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {
    boolean existsById(String id);

    boolean existsByNameEqualsIgnoreCase(String name);
}
