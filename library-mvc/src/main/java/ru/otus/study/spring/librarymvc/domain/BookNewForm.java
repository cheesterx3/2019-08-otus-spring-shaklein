package ru.otus.study.spring.librarymvc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookNewForm {
    private String id;
    @NotEmpty
    private String name;
    @NotNull
    private String genreId;
    @NotNull
    private String authorId;
}
