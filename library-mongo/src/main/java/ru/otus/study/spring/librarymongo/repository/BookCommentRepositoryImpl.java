package ru.otus.study.spring.librarymongo.repository;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.study.spring.librarymongo.domain.BookComment;

@RequiredArgsConstructor
public class BookCommentRepositoryImpl implements BookCommentRepositoryCustomized {
    private final MongoTemplate mongoTemplate;

    @Override
    public void removeCommentsForBookWithId(String bookId) {
        val query = Query.query(Criteria.where("book.$id").is(new ObjectId(bookId)));
        mongoTemplate.remove(query, BookComment.class);
    }
}
