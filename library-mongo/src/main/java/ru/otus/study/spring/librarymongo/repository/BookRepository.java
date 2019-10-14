package ru.otus.study.spring.librarymongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.study.spring.librarymongo.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustomized {
    List<Book> findAllByNameLike(String name);

    boolean existsByIdAndAuthorsContains(String bookId, @Param("_id") String authorId);

    boolean existsByAuthorsContains(@Param("_id") String authorId);



    @Query(value = "{'$and': [{'_id':?0}, {'genres.name' : {$regex: ?1, $options: 'i' }}]}", fields = "{_id : 1}")
    Optional<Book> findWithOnlyIdByIdAndGenresContains(String bookId, String genreName);
}
