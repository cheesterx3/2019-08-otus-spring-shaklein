package ru.otus.study.spring.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StudentTask {
    private final String question;

    public StudentTask(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

}
