package ru.otus.study.spring.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class LocaleConfig {

    @Bean("messageSource")
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource=new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/i18n/message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
