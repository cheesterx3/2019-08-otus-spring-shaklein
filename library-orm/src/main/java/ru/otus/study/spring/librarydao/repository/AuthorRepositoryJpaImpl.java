package ru.otus.study.spring.librarydao.repository;

import org.springframework.stereotype.Repository;
import ru.otus.study.spring.librarydao.model.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryJpaImpl implements AuthorRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Author> getById(long id) {
        return Optional.ofNullable(em.find(Author.class,id));
    }

    @Override
    public List<Author> getAll() {
        final TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public boolean deleteById(long authorId) {
        final Optional<Author> author = getById(authorId);
        author.ifPresent(a -> em.remove(a));
        return author.isPresent();
    }

    @Override
    public Author insert(String authorName) {
        Author author = new Author(authorName);
        em.persist(author);
        return author;
    }

}
