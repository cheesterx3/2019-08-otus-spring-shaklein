package ru.otus.study.spring.librarymvc.service;

import ru.otus.study.spring.librarymvc.domain.Book;

public interface LibraryPermissionAttachService {
    Book addDefaultPermissionsForBook(Book book);
}
