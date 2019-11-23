package ru.otus.study.spring.librarymvc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.study.spring.librarymvc.domain.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByLogin(String login);
}
