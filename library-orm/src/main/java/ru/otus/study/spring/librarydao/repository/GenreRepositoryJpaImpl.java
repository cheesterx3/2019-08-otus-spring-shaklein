package ru.otus.study.spring.librarydao.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.study.spring.librarydao.helper.GenericDaoResult;
import ru.otus.study.spring.librarydao.model.Genre;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Objects;
import java.util.Optional;

@Repository
public class GenreRepositoryJpaImpl implements GenreRepository {
    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<Genre> getById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public GenericDaoResult<Genre> getByName(String name) {
        if (Objects.isNull(name)) {
            return GenericDaoResult.createError("Generic name cannot be null");
        }
        final TypedQuery<Genre> query = em.createQuery("select g from Genre g where lower(g.name)=:name", Genre.class);
        query.setParameter("name", name.toLowerCase());
        try {
            return GenericDaoResult.createResult(query.getSingleResult());
        } catch (EmptyResultDataAccessException | NoResultException e) {
            return GenericDaoResult.createError("No genre found");
        }

    }

    @Override
    @Transactional
    public Genre insert(String genreName) {
        Objects.requireNonNull(genreName,"Genre name cannot be null");
        final Genre genre = new Genre(genreName);
        em.persist(genre);
        return genre;
    }

}
