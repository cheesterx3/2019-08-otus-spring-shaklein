package ru.otus.study.spring.librarydao.repository;

import org.springframework.stereotype.Repository;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.BookComment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BookCommentRepositoryJpaImpl implements BookCommentRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public BookComment commentBook(BookComment bookComment) {
        em.persist(bookComment);
        return bookComment;
    }

    @Override
    public List<BookComment> getBookComments(Book book) {
        final TypedQuery<BookComment> query = em.createQuery("select c from BookComment c where c.book=:book", BookComment.class);
        query.setParameter("book",book);
        return query.getResultList();
    }

}
