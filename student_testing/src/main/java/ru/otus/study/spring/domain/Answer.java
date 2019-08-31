package ru.otus.study.spring.domain;

public class Answer {
    private final String content;

    public Answer(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }
}
