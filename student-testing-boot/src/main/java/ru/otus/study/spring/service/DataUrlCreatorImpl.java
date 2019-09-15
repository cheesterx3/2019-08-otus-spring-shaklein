package ru.otus.study.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.config.ApplicationProperties;

@Component
public class DataUrlCreatorImpl implements DataUrlCreator {

    private final String dataUrl;
    private final String localeTag;
    private final ApplicationProperties applicationProperties;

    public DataUrlCreatorImpl(ApplicationProperties applicationProperties) {
        this.dataUrl = applicationProperties.getDataURL();
        this.localeTag = applicationProperties.getLocale();
        this.applicationProperties = applicationProperties;
    }

    @Override
    public String createDaoURL() {
        String urlAsString = createUrlForLocale();
        if (getClass().getResource("/" + urlAsString) == null) {
            return dataUrl.concat(".csv");
        }
        return urlAsString;
    }

    private String createUrlForLocale() {
        return dataUrl
                .concat("_")
                .concat(localeTag.replaceAll("-", "_"))
                .concat(".csv");
    }
}
