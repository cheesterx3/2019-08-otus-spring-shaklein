package ru.otus.study.spring.service.i18n;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service("messageLocalizationService")
public class LocalizationServiceImpl implements LocalizationService {

    private final String langTag;
    private final MessageSource messageSource;

    LocalizationServiceImpl(@Value("${app.locale}") String langTag, MessageSource messageSource) {
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
