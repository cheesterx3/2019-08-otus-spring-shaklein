package ru.otus.study.spring.batch.librarymigration.component;

import ru.otus.study.spring.batch.librarymigration.domain.jdbc.*;
import ru.otus.study.spring.batch.librarymigration.domain.mongo.*;

public interface DomainTransformer {
    AuthorMongo toMongoAuthor(Author author);

    GenreMongo toMongoGenre(Genre genre);

    RoleMongo toMongoRole(Role role);

    UserMongo toMongoUser(User user);

    BookMongo toMongoBook(Book book);

    BookCommentMongo toMongoComment(BookComment bookComment);
}
