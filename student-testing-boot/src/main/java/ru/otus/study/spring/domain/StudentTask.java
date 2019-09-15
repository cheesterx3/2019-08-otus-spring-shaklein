package ru.otus.study.spring.domain;

public class StudentTask {
    private final String question;

    public StudentTask(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

}
