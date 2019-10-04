package ru.otus.study.spring.librarydao.repository;

import org.springframework.stereotype.Repository;
import ru.otus.study.spring.librarydao.model.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class BookRepositoryJpaImpl implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    private final GenreRepository genreRepository;

    public BookRepositoryJpaImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }


    @Override
    public Optional<Book> getById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> getAll() {
        return em.createQuery("select b from Book b", Book.class).getResultList();
    }

    @Override
    public void delete(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        em.remove(book);
    }

    @Override
    public Book insert(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        em.persist(book);
        return book;
    }


}
