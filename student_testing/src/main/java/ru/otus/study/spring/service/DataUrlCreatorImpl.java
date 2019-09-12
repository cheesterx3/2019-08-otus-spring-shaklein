package ru.otus.study.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DataUrlCreatorImpl implements DataUrlCreator {

    private final String dataUrl;
    private final String localeTag;

    public DataUrlCreatorImpl(@Value("${data.file}") String dataUrl, @Value("${app.locale}") String localeTag) {
        this.dataUrl = dataUrl;
        this.localeTag = localeTag;
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
