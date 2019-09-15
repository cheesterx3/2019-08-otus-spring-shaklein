package ru.otus.study.spring.service.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.config.ApplicationProperties;

import java.util.Locale;

@Service("messageLocalizationService")
public class LocalizationServiceImpl implements LocalizationService {

    private final String langTag;
    private final MessageSource messageSource;
    private final ApplicationProperties applicationProperties;

    LocalizationServiceImpl(MessageSource messageSource, ApplicationProperties applicationProperties) {
        this.langTag = applicationProperties.getLocale();
        this.messageSource = messageSource;
        this.applicationProperties = applicationProperties;
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
