package ru.otus.study.spring.libraryweb.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.domain.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDtoSimple {
    private String id;
    private String name;
    private String authorsInfo;
    private String genresInfo;
    private List<String> authorId;
    private List<String> genreId;

    public static BookDtoSimple toDto(Book book) {
        return new BookDtoSimple(book.getId(),
                book.getName(),
                book.getAuthorsInfo(),
                book.getGenresInfo(),
                book.getAuthors().stream().map(Author::getId).collect(Collectors.toList()),
                book.getGenres().stream().map(Genre::getId).collect(Collectors.toList())
                );
    }
}
