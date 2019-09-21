package ru.otus.study.spring.librarydao.model;

public class Author {
    private final long id;
    private final String name;

    public Author(long id, String name) {
        this.id = id;
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
