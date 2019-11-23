package ru.otus.study.spring.librarymvc.events;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import ru.otus.study.spring.librarymvc.domain.Book;
import ru.otus.study.spring.librarymvc.domain.Genre;
import ru.otus.study.spring.librarymvc.repository.BookRepository;

@Component
@RequiredArgsConstructor
public class MongoGenreEventsListener extends CustomizedAbstractMongoEventListener<Genre> {
    private final BookRepository bookRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Genre> event) {
        super.onBeforeDelete(event);
        final String id = getObjectId(event);
        if (bookRepository.hasBookWithSingleGenreId(id)) {
            throw new RuntimeException("Unable to remove genre, cause it assigned to book(s). Remove genre from book(s) first");
        }
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<Genre> event) {
        super.onAfterDelete(event);
        final String id = getObjectId(event);
        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
        Update update = new Update().pull("genres", query);
        mongoTemplate.updateMulti(new Query(), update, Book.class);
    }


}

