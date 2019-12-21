package ru.otus.study.spring.libraryweb.components;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Genre;
import ru.otus.study.spring.libraryweb.repository.BookRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LibraryInfoContributor implements InfoContributor {
    private final BookRepository bookRepository;

    @Override
    public void contribute(Info.Builder builder) {
        final long booksCount = bookRepository.count();
        final Map<Genre, Long> booksByGenre = bookRepository.countBooksByGenre();
        final Map<Author, Long> booksByAuthor = bookRepository.countBooksByAuthor();
        builder.withDetail("LibraryInfo", new LibraryInfo(booksCount, booksByGenre, booksByAuthor)).build();
    }

    private static class LibraryInfo extends HashMap<String, Object> {

        public LibraryInfo(long booksCount, Map<Genre, Long> booksByGenre, Map<Author, Long> booksByAuthor) {
            final Map<String, Object> genresInfo = booksByGenre.entrySet()
                    .stream()
                    .collect(Collectors.toMap(entry -> entry.getKey().getName(), Entry::getValue));
            final Map<String, Object> authorsInfo = booksByAuthor.entrySet()
                    .stream()
                    .collect(Collectors.toMap(entry -> entry.getKey().getName(), Entry::getValue));
            put("BooksCount", booksCount);
            put("GenresInfo", genresInfo);
            put("AuthorsInfo", authorsInfo);
        }
    }

}
