package ru.otus.study.spring.libraryweb.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.study.spring.libraryweb.rest.dto.AuthorDto;
import ru.otus.study.spring.libraryweb.rest.dto.GenreDto;
import ru.otus.study.spring.libraryweb.service.LibraryReaderService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final LibraryReaderService readerService;

    @GetMapping("/api/authors")
    public List<AuthorDto> getAll() {
        return readerService.findAllAuthors()
                .stream().map(AuthorDto::toDto)
                .collect(toList());
    }
}
