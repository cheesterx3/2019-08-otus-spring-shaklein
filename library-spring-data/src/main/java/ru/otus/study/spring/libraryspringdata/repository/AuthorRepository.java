package ru.otus.study.spring.libraryspringdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.study.spring.libraryspringdata.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
