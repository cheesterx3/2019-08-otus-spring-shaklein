package ru.otus.study.spring.batch.librarymigration.component;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import ru.otus.study.spring.batch.librarymigration.domain.jdbc.*;
import ru.otus.study.spring.batch.librarymigration.domain.mongo.*;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@RequiredArgsConstructor
public class DomainTransformer {
    private final MongoTemplate mongoTemplate;

    public AuthorMongo toMongoAuthor(Author author) {
        final Optional<AuthorMongo> authorMongo = findEntity(author.getId(), AuthorMongo.class);
        return authorMongo.orElse(new AuthorMongo(author.getId(), author.getName()));
    }

    public GenreMongo toMongoGenre(Genre genre) {
        final Optional<GenreMongo> genreMongo = findEntity(genre.getId(), GenreMongo.class);
        return genreMongo.orElse(new GenreMongo(genre.getId(), genre.getName()));
    }

    public RoleMongo toMongoRole(Role role) {
        final Optional<RoleMongo> roleMongo = findEntity(role.getId(), RoleMongo.class);
        return roleMongo.orElse(new RoleMongo(role.getId(), role.getRole()));
    }

    public UserMongo toMongoUser(User user) {
        final Optional<UserMongo> userMongo = findEntity(user.getId(), UserMongo.class);
        final List<RoleMongo> roleMongoList = findEntities(user.getRoles().stream().map(Role::getId).collect(toList()), RoleMongo.class);
        return userMongo.map(us -> {
            us.setRoles(roleMongoList);
            return us;
        }).orElse(new UserMongo(user.getId(), user.getName(), user.getLogin(), user.getPassword(), roleMongoList));
    }

    public BookMongo toMongoBook(Book book) {
        final Optional<BookMongo> bookMongo = findEntity(book.getId(), BookMongo.class);

        final List<AuthorMongo> authorMongoList = findEntities(book.getAuthors().stream().map(Author::getId).collect(toList()), AuthorMongo.class);
        final List<GenreMongo> genreMongoList = findEntities(book.getGenres().stream().map(Genre::getId).collect(toList()), GenreMongo.class);
        return bookMongo.map(b -> {
            b.setGenres(genreMongoList);
            b.setAuthors(authorMongoList);
            return b;
        }).orElse(new BookMongo(book.getId(), book.getName(), authorMongoList, genreMongoList));
    }

    public BookCommentMongo toMongoComment(BookComment bookComment) {
        final Optional<BookCommentMongo> bookCommentMongo = findEntity(bookComment.getId(), BookCommentMongo.class);
        final BookMongo bookMongo = toMongoBook(bookComment.getBook());
        final UserMongo userMongo = toMongoUser(bookComment.getUser());
        return bookCommentMongo.map(comment -> {
            comment.setBook(bookMongo);
            return comment;
        }).orElse(new BookCommentMongo(bookComment.getId(), bookComment.getText(), bookComment.getTime(), bookMongo, userMongo));
    }

    private <T> Optional<T> findEntity(Long migrateId, Class<T> objClass) {
        final Criteria criteria = Criteria.where("migrateId").is(migrateId);
        return Optional.ofNullable(mongoTemplate.findOne(query(criteria), objClass));
    }

    private <T> List<T> findEntities(List<Long> migrateIdList, Class<T> objClass) {
        final Criteria criteria = Criteria.where("migrateId").in(migrateIdList);
        return mongoTemplate.find(query(criteria), objClass);
    }

}
