package ru.otus.study.spring.libraryweb.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.study.spring.libraryweb.domain.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
    @Override
    Flux<Author> findAllById(Iterable<String> iterable);
}
