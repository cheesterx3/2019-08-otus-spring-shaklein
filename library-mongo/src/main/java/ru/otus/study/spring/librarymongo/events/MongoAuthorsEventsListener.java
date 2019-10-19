package ru.otus.study.spring.librarymongo.events;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import ru.otus.study.spring.librarymongo.domain.Author;
import ru.otus.study.spring.librarymongo.domain.Book;
import ru.otus.study.spring.librarymongo.repository.BookRepository;

@Component
@RequiredArgsConstructor
public class MongoAuthorsEventsListener extends CustomizedAbstractMongoEventListener<Author> {
    private final BookRepository bookRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
        super.onBeforeDelete(event);
        final String id=getObjectId(event);
        if (bookRepository.hasBookWithSingleAuthorId(id)) {
            throw new RuntimeException("Unable to remove author, cause it assigned to book(s). Remove author from book(s) first");
        }
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<Author> event) {
        super.onAfterDelete(event);
        final String id = getObjectId(event);
        Query query = new Query(Criteria.where("$id").is(new ObjectId(id)));
        Update update = new Update().pull("authors", query);
        mongoTemplate.updateMulti(new Query(), update, Book.class);
    }

}
