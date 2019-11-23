package ru.otus.study.spring.librarymvc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comment")
@Data
@AllArgsConstructor
public class BookComment {
    @Id
    private String id;
    private String text;
    private LocalDateTime time;
    @DBRef
    private Book book;
    @DBRef
    private User user;

    public BookComment() {
        time = LocalDateTime.now();
    }

    public BookComment(String text, Book book, User user) {
        this();
        this.text = text;
        this.book = book;
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", time, text);
    }
}
