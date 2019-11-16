package ru.otus.study.spring.libraryweb.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoForSave {
    private String name;
    private List<String> authorId;
    private List<String> genreId;

    public Flux<String> getAuthorsFlux() {
        return Flux.fromStream(authorId.stream());
    }

    public Flux<String> getGenresFlux() {
        return Flux.fromStream(genreId.stream());
    }

}
