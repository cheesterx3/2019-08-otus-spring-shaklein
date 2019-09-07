package ru.otus.study.spring.service.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

abstract class AbstractLocalizationService implements LocalizationService {
    private final String langTag;
    private final MessageSource messageSource;

    AbstractLocalizationService(String langTag, MessageSource messageSource) {
        this.langTag = langTag;
        this.messageSource = messageSource;
    }

    @Override
    public String getLocalized(String ident) {
        try {
            return messageSource.getMessage(ident, null, Locale.forLanguageTag(langTag));
        } catch (NoSuchMessageException e) {
            return ident;
        }
    }

    @Override
    public String getLocalized(String ident, Object[] objects) {
        try {
            return messageSource.getMessage(ident, objects, Locale.forLanguageTag(langTag));
        } catch (NoSuchMessageException e) {
            return ident;
        }
    }
}
