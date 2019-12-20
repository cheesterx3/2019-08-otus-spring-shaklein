package ru.otus.study.spring.libraryweb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.domain.Genre;

import java.util.List;
import java.util.Map;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustomized {
    List<Book> findAllByNameLike(String name);

    boolean existsByIdAndAuthorsContains(String bookId, @Param("_id") String authorId);

    boolean existsByAuthorsContains(@Param("_id") String authorId);

}


