package ru.otus.study.spring.librarydao.repository.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.study.spring.librarydao.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookAuthorsExtractor implements ResultSetExtractor<Map<Long, List<Author>>> {

    @Override
    public Map<Long, List<Author>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        final Map<Long, List<Author>> authorMap = new HashMap<>();
        while (resultSet.next()) {
            final long book_id = resultSet.getLong("book_id");
            if (!authorMap.containsKey(book_id)) {
                authorMap.put(book_id, new ArrayList<>());
            }
            authorMap.get(book_id).add(new Author(resultSet.getLong("id"), resultSet.getString("name")));
        }
        return authorMap;
    }
}
