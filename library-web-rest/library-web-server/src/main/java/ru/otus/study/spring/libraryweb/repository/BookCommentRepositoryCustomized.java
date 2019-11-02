package ru.otus.study.spring.libraryweb.repository;

public interface BookCommentRepositoryCustomized {
    void removeCommentsForBookWithId(String bookId);
}
