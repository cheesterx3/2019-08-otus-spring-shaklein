package ru.otus.study.spring.libraryweb.service;

import org.springframework.lang.Nullable;

public interface LocalizationService {
    String getLocalized(String ident);

    String getLocalized(String ident, @Nullable Object[] var2);
}

