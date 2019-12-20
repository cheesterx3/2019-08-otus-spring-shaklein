package ru.otus.study.spring.libraryweb.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.study.spring.libraryweb.rest.dto.GenreDto;
import ru.otus.study.spring.libraryweb.service.LibraryReaderService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class GenreController {
    private final LibraryReaderService readerService;

    @GetMapping("/api/genres")
    public List<GenreDto> getAll() {
        return readerService.findAllGenres()
                .stream().map(GenreDto::toDto)
                .collect(toList());
    }
}
