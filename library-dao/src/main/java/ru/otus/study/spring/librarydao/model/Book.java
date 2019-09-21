package ru.otus.study.spring.librarydao.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Book {
    private final long id;
    private final String name;
    private final List<Author> authors;
    private final List<Genre> genres;


    public Book(long id, String name) {
        this.id = id;
        this.name = name;
        authors = new ArrayList<>();
        genres = new ArrayList<>();
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
