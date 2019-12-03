package ru.otus.study.spring.librarymvc.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.study.spring.librarymvc.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustomized {

    @PostFilter(value = "hasPermission(filterObject,'READ')")
    List<Book> findAllByNameLike(String name);

    boolean existsByIdAndAuthorsContains(String bookId, @Param("_id") String authorId);

    boolean existsByAuthorsContains(@Param("_id") String authorId);

    @Override
    @PreAuthorize(value = "hasPermission(#book,'ADMINISTRATION')")
    void delete(Book book);

    @Override
    @PreAuthorize(value = "hasPermission(#id,'ru.otus.study.spring.librarymvc.domain.Book','READ')")
    Optional<Book> findById(String id);

    @Override
    @PostFilter(value = "hasPermission(filterObject,'READ')")
    List<Book> findAll(Sort sort);

    @Override
    @PostFilter(value = "hasPermission(filterObject,'READ')")
    List<Book> findAll();
}


