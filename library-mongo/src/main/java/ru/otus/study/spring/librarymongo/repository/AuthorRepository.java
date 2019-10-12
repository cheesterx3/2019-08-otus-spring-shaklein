package ru.otus.study.spring.librarymongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.study.spring.librarymongo.domain.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {

}
