package ru.otus.study.spring.librarydao.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;

    public Author() {
    }

    public Author(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%d - %s", id, name);
    }
}
