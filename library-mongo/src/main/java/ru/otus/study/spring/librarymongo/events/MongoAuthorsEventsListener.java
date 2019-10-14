package ru.otus.study.spring.librarymongo.events;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.study.spring.librarymongo.domain.Author;
import ru.otus.study.spring.librarymongo.domain.Genre;
import ru.otus.study.spring.librarymongo.repository.BookRepository;

@Component
@RequiredArgsConstructor
public class MongoAuthorsEventsListener extends AbstractMongoEventListener<Author> {
    private final BookRepository bookRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
        super.onBeforeDelete(event);
        final Document source = event.getSource();
        final String id = source.get("_id").toString();
        if (bookRepository.existsByAuthorsContains(id)) {
            throw new RuntimeException("Unable to remove author, cause it assigned to book(s). Remove author from book(s) first");
        }
    }
}
