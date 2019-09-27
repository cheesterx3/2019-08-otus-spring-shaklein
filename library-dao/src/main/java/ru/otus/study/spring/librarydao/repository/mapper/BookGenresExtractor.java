package ru.otus.study.spring.librarydao.repository.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.study.spring.librarydao.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookGenresExtractor implements ResultSetExtractor<Map<Long, List<Genre>>> {

    @Override
    public Map<Long, List<Genre>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        final Map<Long, List<Genre>> genreMap = new HashMap<>();
        while (resultSet.next()) {
            final long book_id = resultSet.getLong("book_id");
            if (!genreMap.containsKey(book_id)) {
                genreMap.put(book_id, new ArrayList<>());
            }
            genreMap.get(book_id).add(new Genre(resultSet.getLong("id"), resultSet.getString("name")));
        }
        return genreMap;
    }
}
