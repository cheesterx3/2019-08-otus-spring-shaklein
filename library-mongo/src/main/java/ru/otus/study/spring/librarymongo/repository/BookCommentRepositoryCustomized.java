package ru.otus.study.spring.librarymongo.repository;

public interface BookCommentRepositoryCustomized {
    void removeCommentsForBookWithId(String bookId);
}
