package ru.otus.study.spring.libraryspringdata.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;

    @ManyToMany(targetEntity = Author.class, fetch = FetchType.EAGER)
    @JoinTable(name = "ref_book_author", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Author> authors;

    @ManyToMany(targetEntity = Genre.class, fetch = FetchType.EAGER)
    @JoinTable(name = "ref_book_genre", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Genre> genres;

    public Book() {
    }

    public Book(String name) {
        this.name = name;
        authors = new ArrayList<>();
        genres = new ArrayList<>();
    }

    public Book(String name, Author author, Genre genre) {
        this(name);
        authors.add(author);
        genres.add(genre);
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%d - %s. %s. (%s)", id, name, getGenresInfo(), getAuthorsInfo());
    }

    private String getAuthorsInfo() {
        return authors.stream().map(Author::getName).collect(Collectors.joining(", "));
    }

    private String getGenresInfo() {
        return genres.stream().map(Genre::getName).collect(Collectors.joining(", "));
    }

}
