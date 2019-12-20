package ru.otus.study.spring.libraryweb.components;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.domain.Genre;
import ru.otus.study.spring.libraryweb.service.BookPopularityService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DefaultBookPopularityService implements BookPopularityService {
    private final MeterRegistry meterRegistry;
    private final Map<Genre, Counter> genreCounterMap = Collections.synchronizedMap(new HashMap<>());

    public void viewBook(Book book) {
        book.getGenres().forEach(genre -> findCounter(genre).increment(1.0));
    }

    private synchronized Counter findCounter(Genre genre) {
        final Counter counter = genreCounterMap.get(genre);
        return genreCounterMap.putIfAbsent(genre, counter == null ? createGenreCounter(genre) : counter);
    }

    private Counter createGenreCounter(Genre genre) {
        return meterRegistry.counter("book.view", "genre", genre.getName());
    }

}
