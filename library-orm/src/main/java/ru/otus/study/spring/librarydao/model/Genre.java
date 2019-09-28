package ru.otus.study.spring.librarydao.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;

    public Genre() {
    }

    public Genre(String name) {
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
