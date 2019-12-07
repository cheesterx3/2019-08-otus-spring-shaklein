package ru.otus.study.spring.batch.librarymigration.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCommentMongo {
    @Id
    private String id;
    @Indexed
    private long migrateId;
    private String text;
    private LocalDateTime time;
    @DBRef
    private BookMongo book;
    @DBRef
    private UserMongo user;

    public BookCommentMongo(long migrateId, String text, LocalDateTime time, BookMongo book, UserMongo user) {
        this.migrateId = migrateId;
        this.text = text;
        this.time = time;
        this.book = book;
        this.user = user;
    }
}
