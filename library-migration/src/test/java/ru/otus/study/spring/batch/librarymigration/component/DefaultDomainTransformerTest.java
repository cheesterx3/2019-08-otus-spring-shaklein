package ru.otus.study.spring.batch.librarymigration.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import ru.otus.study.spring.batch.librarymigration.domain.jdbc.*;
import ru.otus.study.spring.batch.librarymigration.domain.mongo.*;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = DefaultDomainTransformer.class,
        properties = {
                ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false",
                InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"
        })
@DisplayName("Сервис преобразования jdbc-объектов в mongo-сущности")
class DefaultDomainTransformerTest {
    @MockBean
    private MongoTemplate mongoTemplate;
    @Autowired
    private DomainTransformer domainTransformer;

    private Author author;
    private Genre genre;
    private Book book;
    private Role role;
    private User user;
    private BookComment bookComment;

    @BeforeEach
    void setUp() {
        author = new Author(1, "Author");
        genre = new Genre(1, "Genre");
        role = new Role(1, "Role");
        user = new User(1, "User", "user", "password", Collections.singletonList(role));
        book = new Book(1, "Book", Collections.singletonList(author), Collections.singletonList(genre));
        bookComment = new BookComment(1, "Test comment", LocalDateTime.now(), book, user);
        given(mongoTemplate.findOne(any(Query.class), any())).willReturn(null);

    }

    @Test
    @DisplayName("корректно переводить jdbc автора в mongo-объект")
    void toMongoAuthor() {
        assertThat(domainTransformer.toMongoAuthor(author))
                .isNotNull()
                .matches(authorMongo -> authorMongo.getMigrateId() == author.getId())
                .matches(authorMongo -> authorMongo.getName().equals(author.getName()));
    }

    @Test
    @DisplayName("корректно переводить jdbc жанра в mongo-объект")
    void toMongoGenre() {
        assertThat(domainTransformer.toMongoGenre(genre))
                .isNotNull()
                .matches(genreMongo -> genreMongo.getMigrateId() == genre.getId())
                .matches(genreMongo -> genreMongo.getName().equals(genre.getName()));
    }

    @Test
    @DisplayName("корректно переводить jdbc роли пользователя в mongo-объект")
    void toMongoRole() {
        assertThat(domainTransformer.toMongoRole(role))
                .isNotNull()
                .matches(roleMongo -> roleMongo.getMigrateId() == role.getId())
                .matches(roleMongo -> roleMongo.getRole().equals(role.getRole()));
    }

    @Test
    @DisplayName("корректно переводить jdbc пользователя в mongo-объект")
    void toMongoUser() {
        given(mongoTemplate.find(any(Query.class), eq(RoleMongo.class))).willReturn(Collections.singletonList(mock(RoleMongo.class)));
        assertThat(domainTransformer.toMongoUser(user))
                .isNotNull()
                .matches(userMongo -> userMongo.getMigrateId() == user.getId())
                .matches(userMongo -> userMongo.getName().equals(user.getName()))
                .matches(userMongo -> userMongo.getLogin().equals(user.getLogin()))
                .matches(userMongo -> userMongo.getPassword().equals(user.getPassword()))
                .extracting(UserMongo::getRoles).asList().hasSize(user.getRoles().size());
    }

    @Test
    @DisplayName("корректно переводить jdbc книги в mongo-объект")
    void toMongoBook() {
        given(mongoTemplate.find(any(Query.class), eq(AuthorMongo.class))).willReturn(Collections.singletonList(mock(AuthorMongo.class)));
        given(mongoTemplate.find(any(Query.class), eq(GenreMongo.class))).willReturn(Collections.singletonList(mock(GenreMongo.class)));
        assertThat(domainTransformer.toMongoBook(book))
                .isNotNull()
                .matches(bookMongo -> bookMongo.getMigrateId() == book.getId())
                .matches(bookMongo -> bookMongo.getName().equals(book.getName()))
                .matches(bookMongo -> bookMongo.getAuthors().size() == book.getAuthors().size())
                .matches(bookMongo -> bookMongo.getGenres().size() == book.getGenres().size());
    }

    @Test
    @DisplayName("корректно переводить jdbc комментарий в mongo-объект")
    void toMongoComment() {
        final BookMongo bookMongo = mock(BookMongo.class);
        final UserMongo userMongo = mock(UserMongo.class);
        given(mongoTemplate.findOne(any(Query.class), eq(BookMongo.class))).willReturn(bookMongo);
        given(mongoTemplate.findOne(any(Query.class), eq(UserMongo.class))).willReturn(userMongo);
        assertThat(domainTransformer.toMongoComment(bookComment))
                .isNotNull()
                .matches(bookCommentMongo -> bookCommentMongo.getMigrateId() == bookComment.getId())
                .matches(bookCommentMongo -> bookCommentMongo.getText().equals(bookComment.getText()))
                .matches(bookCommentMongo -> bookCommentMongo.getTime().equals(bookComment.getTime()))
                .matches(bookCommentMongo -> bookCommentMongo.getTime().equals(bookComment.getTime()))
                .matches(bookCommentMongo -> bookCommentMongo.getBook() == bookMongo)
                .matches(bookCommentMongo -> bookCommentMongo.getUser() == userMongo);
    }
}