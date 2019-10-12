package ru.otus.study.spring.librarymongo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.librarymongo.domain.Author;
import ru.otus.study.spring.librarymongo.domain.Book;
import ru.otus.study.spring.librarymongo.domain.Genre;
import ru.otus.study.spring.librarymongo.exception.DaoException;
import ru.otus.study.spring.librarymongo.repository.AuthorRepository;
import ru.otus.study.spring.librarymongo.repository.BookRepository;
import ru.otus.study.spring.librarymongo.repository.GenreRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LibraryStorageServiceImpl implements LibraryStorageService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> addNewBook(String name, String authorId, String genreName) throws DaoException {
        if (Objects.isNull(genreName)) {
            throw new DaoException("Genre name cannot be null");
        }
        final Optional<Author> authorOptional = authorRepository.findById(authorId);
        final Author author = authorOptional.orElseThrow(() -> new DaoException("Author not found"));

        final Optional<Genre> genreInfo = genreRepository.findByNameIgnoreCase(genreName);
        final Genre genre = genreInfo.orElseGet(() -> genreRepository.save(new Genre(genreName)));
        return Optional.of(bookRepository.save(new Book(name, author, genre)));
    }

    @Override
    public boolean deleteBook(String bookId) {
        final Optional<Book> book = bookRepository.findById(bookId);
        book.ifPresent(bookRepository::delete);
        return book.isPresent();
    }
}
