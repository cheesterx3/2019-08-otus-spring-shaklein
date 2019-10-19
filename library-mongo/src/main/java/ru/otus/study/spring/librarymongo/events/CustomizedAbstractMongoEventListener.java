package ru.otus.study.spring.librarymongo.events;

import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

public abstract class CustomizedAbstractMongoEventListener<T> extends AbstractMongoEventListener<T> {

    String getObjectId(AbstractDeleteEvent<T> event) {
        final Document source = event.getSource();
        return source.get("_id").toString();
    }
}
