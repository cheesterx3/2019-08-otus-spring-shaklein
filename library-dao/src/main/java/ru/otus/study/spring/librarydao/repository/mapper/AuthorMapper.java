package ru.otus.study.spring.librarydao.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.study.spring.librarydao.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        return new Author(id, name);
    }
}
