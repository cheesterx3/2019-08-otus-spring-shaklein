package ru.otus.study.spring.librarymongo.events;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.study.spring.librarymongo.domain.Genre;
import ru.otus.study.spring.librarymongo.repository.BookRepository;

@Component
@RequiredArgsConstructor
public class MongoGenreEventsListener extends AbstractMongoEventListener<Genre> {
    private final BookRepository bookRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Genre> event) {
        super.onBeforeDelete(event);
        final Document source = event.getSource();
        final String id = source.get("_id").toString();
        if (bookRepository.existsByGenresContains(id)) {
            throw new RuntimeException("Unable to remove genre, cause it assigned to book(s). Remove genre from book(s) first");
        }
    }
}
