package ru.otus.study.spring.librarydao.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.study.spring.librarydao.model.Genre;
import ru.otus.study.spring.librarydao.repository.mapper.GenreMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GenreRepositoryJdbcImpl implements GenreRepository {
    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public GenreRepositoryJdbcImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from genre", Integer.class);

    }

    @Override
    public Optional<Genre> getById(long id) {
        Genre genre;
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            genre = namedParameterJdbcOperations.queryForObject("select * from genre where id = :id", params, new GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            genre = null;
        }
        return Optional.ofNullable(genre);
    }

    @Override
    public List<Genre> getAll() {
        return namedParameterJdbcOperations.query("select * from genre ", new GenreMapper());
    }

    @Override
    public boolean deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.update("delete from genre where id = :id", params) == 1;
    }

    @Override
    public Genre insert(String genreName) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            String[] params = new String[]{genreName};
            PreparedStatement ps = connection.prepareStatement("insert into genre (`name`) values (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, params[0]);
            return ps;
        }, keyHolder);
        long genreID = (long) keyHolder.getKey();
        return new Genre(genreID, genreName);
    }

}
