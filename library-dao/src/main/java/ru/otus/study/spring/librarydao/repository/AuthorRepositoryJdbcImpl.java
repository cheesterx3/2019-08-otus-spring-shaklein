package ru.otus.study.spring.librarydao.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.study.spring.librarydao.model.Author;
import ru.otus.study.spring.librarydao.repository.mapper.AuthorMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class AuthorRepositoryJdbcImpl implements AuthorRepository {
    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public AuthorRepositoryJdbcImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from author", Integer.class);

    }

    @Override
    public Optional<Author> getById(long id) {
        Author author;
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            author = namedParameterJdbcOperations.queryForObject("select * from author where id = :id", params, new AuthorMapper());
        } catch (EmptyResultDataAccessException e) {
            author = null;
        }
        return Optional.ofNullable(author);
    }

    @Override
    public List<Author> getAll() {
        return namedParameterJdbcOperations.query("select * from author ", new AuthorMapper());
    }

    @Override
    public boolean deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.update("delete from author where id = :id", params) == 1;
    }

    @Override
    public Author insert(String authorName) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            String[] params = new String[]{authorName};
            PreparedStatement ps = connection.prepareStatement("insert into author (`name`) values (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, params[0]);
            return ps;
        }, keyHolder);
        long authorID = (long) keyHolder.getKey();
        return new Author(authorID, authorName);
    }

}
