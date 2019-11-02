package ru.otus.study.spring.libraryweb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.study.spring.libraryweb.domain.Genre;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String> {
    Optional<Genre> findByNameIgnoreCase(String name);

}
