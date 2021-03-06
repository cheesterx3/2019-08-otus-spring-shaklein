package ru.otus.study.spring.libraryweb.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.study.spring.libraryweb.domain.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

}
