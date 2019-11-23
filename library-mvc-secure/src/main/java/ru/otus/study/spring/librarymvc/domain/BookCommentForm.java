package ru.otus.study.spring.librarymvc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookCommentForm {
    private String id;
    @NotEmpty
    private String comment;
}
