package ru.otus.study.spring.libraryspringdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.study.spring.libraryspringdata.domain.Genre;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByNameIgnoreCase(String name);
}
