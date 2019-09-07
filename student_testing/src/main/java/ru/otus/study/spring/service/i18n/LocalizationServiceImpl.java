package ru.otus.study.spring.service.i18n;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service("messageLocalizationService")
public class LocalizationServiceImpl extends AbstractLocalizationService {

    public LocalizationServiceImpl(@Value("${app.locale}") String langTag, @Qualifier("messageSource") MessageSource messageSource) {
        super(langTag, messageSource);
    }
}
