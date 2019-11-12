package ru.otus.study.spring.libraryweb.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.study.spring.libraryweb.domain.Genre;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    private String id;
    private String name;

    public static GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
