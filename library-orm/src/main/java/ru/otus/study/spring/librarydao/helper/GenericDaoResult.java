package ru.otus.study.spring.librarydao.helper;

import ru.otus.study.spring.librarydao.model.Book;

import java.util.Optional;

public class GenericDaoResult<T> extends Book {
    private final T result;
    private final String error;

    private GenericDaoResult(T result, String error) {
        this.result = result;
        this.error = error;
    }

    public static<T> GenericDaoResult<T> createResult(T result){
        return new GenericDaoResult<>(result,"");
    }

    public static<T> GenericDaoResult<T> createError(String error){
        return new GenericDaoResult<>(null,error);
    }

    public String getError() {
        return error;
    }

    public Optional<T> getResult() {
        return Optional.ofNullable(result);
    }
}
