package ru.otus.study.spring.libraryweb.repository;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.domain.Genre;
import ru.otus.study.spring.libraryweb.exception.DaoException;
import ru.otus.study.spring.libraryweb.exception.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RequiredArgsConstructor
public class BookRepositoryCustomizedImpl implements BookRepositoryCustomized {
    private final MongoTemplate mongoTemplate;

    @Data
    private class ArraySizeProjection {
        private int size;
    }

    @Override
    public void removeGenreFromBookByBookId(String bookId, String genreId) throws DaoException, NotFoundException {
        checkForGenreAvailableToRemoveFromBook(bookId, genreId);
        Query query = new Query(Criteria.where("_id").is(new ObjectId(genreId)));
        Update update = new Update().pull("genres", query);
        mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(bookId))), update, Book.class);
    }

    private void checkForGenreAvailableToRemoveFromBook(String bookId, String genreId) throws DaoException, NotFoundException {
        final List<Genre> genresForBookId = findGenresForBookId(bookId);
        if (genresForBookId.isEmpty()) {
            throw new NotFoundException(String.format("Book with id [%s] not found", bookId));
        }
        if (genresForBookId.stream().noneMatch(genre -> genre.getId().equals(genreId))) {
            throw new NotFoundException("Book doesn't contain this genre");
        }
        if (genresForBookId.size() == 1) {
            throw new DaoException("Unable to remove single genre from book. Add any other genre to remove this");
        }
    }

    @Override
    public List<Genre> findGenresForBookId(String bookId) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("id").is(bookId))
                , unwind("genres")
                , project().andExclude("_id").and("genres.id").as("_id").and("genres.name").as("name")
        );
        return mongoTemplate.aggregate(aggregation, Book.class, Genre.class).getMappedResults();
    }


    @Override
    public void removeAuthorFromBookByBookId(String bookId, String authorId) throws DaoException {
        if (isNotSingleBookAuthor(bookId)) {
            Query query = new Query(Criteria.where("$id").is(new ObjectId(authorId)));
            Update update = new Update().pull("authors", query);
            mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(bookId))), update, Book.class);
        } else {
            throw new DaoException("Unable to remove single author from book. Add any other author to remove this");
        }
    }

    @Override
    public void addAuthorToBook(String bookId, Author author) {
        Objects.requireNonNull(author, "Author cannot be null");
        Query query = new Query(Criteria.where("_id").is(new ObjectId(bookId)));
        Update update = new Update().push("authors", author);
        mongoTemplate.updateFirst(query, update, Book.class);
    }

    @Override
    public void addGenreToBook(String bookId, Genre genre) {
        Objects.requireNonNull(genre, "Genre cannot be null");
        Query query = new Query(Criteria.where("_id").is(new ObjectId(bookId)));
        Update update = new Update().push("genres", genre);
        mongoTemplate.updateFirst(query, update, Book.class);
    }

    public boolean existsByIdAndGenresContains(String bookId, String genreName) {
        Query query = new Query((Criteria.where("_id").is(new ObjectId(bookId))).and("genres.name").regex(genreName, "i"));
        return mongoTemplate.exists(query, Book.class);
    }

    @Override
    public boolean existsByIdAndGenresContainsById(String bookId, String genreId) {
        Query query = new Query((Criteria.where("_id").is(new ObjectId(bookId))).and("genres.id").is(new ObjectId(genreId)));
        return mongoTemplate.exists(query, Book.class);
    }

    private boolean isNotSingleBookAuthor(String bookId) {
        val aggregation = Aggregation.newAggregation(
                match(where("id").is(bookId)),
                project().andExclude("_id").and("authors").size().as("size"));
        val arraySizeProjection = mongoTemplate.aggregate(aggregation, Book.class, ArraySizeProjection.class).getUniqueMappedResult();
        return Objects.nonNull(arraySizeProjection) && (arraySizeProjection.size > 1);
    }

    public boolean hasBookWithSingleAuthorId(String authorId) {
        Query query = new Query(Criteria.where("authors").size(1).and("authors.$id").is(new ObjectId(authorId)));
        return mongoTemplate.exists(query, Book.class);
    }

    public boolean hasBookWithSingleGenreId(String genreId) {
        Query query = new Query(Criteria.where("genres").size(1).and("genres._id").is(new ObjectId(genreId)));
        return mongoTemplate.exists(query, Book.class);
    }

    @Override
    public Map<Genre, Long> countBooksByGenre() {
        final Aggregation aggregation = createAggregation("genres");
        final List<GenreSizeProjection> mappedResults = mongoTemplate.aggregate(aggregation, Book.class, GenreSizeProjection.class).getMappedResults();
        return mappedResults.stream().collect(Collectors.toMap(SizeProjection::getObject, SizeProjection::getCount));
    }

    @Override
    public Map<Author, Long> countBooksByAuthor() {
        final Aggregation aggregation = createAggregation("authors");
        final List<AuthorSizeProjection> mappedResults = mongoTemplate.aggregate(aggregation, Book.class, AuthorSizeProjection.class).getMappedResults();
        return mappedResults.stream().collect(Collectors.toMap(SizeProjection::getObject, SizeProjection::getCount));
    }

    private Aggregation createAggregation(String authors) {
        return newAggregation(unwind(authors)
                , group(authors).count().as("totalCount")
                , project().and("_id").as("object").and("totalCount").as("count"));
    }

    private static class GenreSizeProjection extends SizeProjection<Genre>{
    }
    private static class AuthorSizeProjection extends SizeProjection<Author>{
    }

    @Data
    private abstract static class SizeProjection<T> {
        private T object;
        private long count;
    }
}
