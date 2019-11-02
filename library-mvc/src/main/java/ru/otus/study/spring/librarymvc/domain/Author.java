package ru.otus.study.spring.librarymvc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "author")
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    private String id;
    private String name;

    public Author(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", id, name);
    }
}
