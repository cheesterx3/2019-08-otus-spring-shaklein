package ru.otus.study.spring.libraryweb.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDtoForSave {
    private String name;
    private List<String> authorId;
    private List<String> genreId;
}
