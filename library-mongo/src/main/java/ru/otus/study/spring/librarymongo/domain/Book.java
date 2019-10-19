package ru.otus.study.spring.librarymongo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private String id;
    private String name;
    @DBRef
    private List<Author> authors;
    private List<Genre> genres;

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

    @Override
    public String toString() {
        return String.format("%s - %s. %s. (%s)", id, name, getGenresInfo(), getAuthorsInfo());
    }

    private String getAuthorsInfo() {
        return authors.stream().map(Author::getName).collect(Collectors.joining(", "));
    }

    private String getGenresInfo() {
        return genres.stream().map(Genre::getName).collect(Collectors.joining(", "));
    }
}
