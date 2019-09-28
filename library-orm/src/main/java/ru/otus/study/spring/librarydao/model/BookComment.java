package ru.otus.study.spring.librarydao.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class BookComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "text")
    private String text;
    @Column(name = "dtime")
    private LocalDateTime time;

    public BookComment() {
    }

    public BookComment(String text) {
        this.text = text;
        time = LocalDateTime.now();
    }


    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", time, text);
    }
}
