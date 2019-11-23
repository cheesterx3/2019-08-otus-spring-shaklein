package ru.otus.study.spring.librarymvc.repository;

public interface BookCommentRepositoryCustomized {
    void removeCommentsForBookWithId(String bookId);
}
