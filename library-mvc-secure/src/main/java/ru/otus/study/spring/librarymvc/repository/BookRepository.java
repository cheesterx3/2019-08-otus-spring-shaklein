package ru.otus.study.spring.librarymvc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.study.spring.librarymvc.domain.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustomized {
    List<Book> findAllByNameLike(String name);

    boolean existsByIdAndAuthorsContains(String bookId, @Param("_id") String authorId);

    boolean existsByAuthorsContains(@Param("_id") String authorId);

}


