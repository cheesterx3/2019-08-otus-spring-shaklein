package ru.otus.study.spring.libraryspringdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.study.spring.libraryspringdata.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
