package ru.otus.study.spring.libraryspringdata.domain;


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
    @OneToOne(targetEntity = Book.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_id")
    private Book book;

    public BookComment() {
    }

    public BookComment(String text) {
        this.text = text;
        time = LocalDateTime.now();
    }

    public BookComment(String text, Book book) {
        this(text);
        this.book = book;
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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
