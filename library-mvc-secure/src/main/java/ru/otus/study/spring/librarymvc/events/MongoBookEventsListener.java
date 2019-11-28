package ru.otus.study.spring.librarymvc.events;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.lang.NonNullApi;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.stereotype.Component;
import ru.otus.study.spring.librarymvc.domain.Book;
import ru.otus.study.spring.librarymvc.repository.BookCommentRepository;

@Component
@RequiredArgsConstructor
public class MongoBookEventsListener extends AbstractMongoEventListener<Book> {
    private final BookCommentRepository commentRepository;
    private final MutableAclService aclService;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Book> event) {
        super.onAfterDelete(event);
        final Document source = event.getSource();
        final String id = source.get("_id").toString();
        commentRepository.removeCommentsForBookWithId(id);
        aclService.deleteAcl(new ObjectIdentityImpl(Book.class, id), false);
    }
}
