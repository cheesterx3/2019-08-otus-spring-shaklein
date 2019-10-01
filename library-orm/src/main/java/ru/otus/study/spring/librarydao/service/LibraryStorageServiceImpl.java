package ru.otus.study.spring.librarydao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.study.spring.librarydao.exception.DaoException;
import ru.otus.study.spring.librarydao.model.Author;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.Genre;
import ru.otus.study.spring.librarydao.repository.AuthorRepository;
import ru.otus.study.spring.librarydao.repository.BookRepository;
import ru.otus.study.spring.librarydao.repository.GenreRepository;

import java.util.Objects;
import java.util.Optional;

@Service
public class LibraryStorageServiceImpl implements LibraryStorageService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    public LibraryStorageServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository, GenreRepository genreRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
    }


    @Override
    @Transactional
    public Optional<Book> addNewBook(String name, long authorId, String genreName) throws DaoException {
        if (Objects.isNull(genreName)) {
            throw new DaoException("Genre name cannot be null");
        }
        final Optional<Author> authorOptional = authorRepository.getById(authorId);
        final Optional<Genre> genreInfo = genreRepository.getByName(genreName);

        final Author author = authorOptional.orElseThrow(() -> new DaoException("Author not found"));
        final Genre genre = genreInfo.orElseGet(()->genreRepository.insert(genreName));
        return Optional.of(bookRepository.insert(new Book(name), author, genre));
    }

    @Override
    @Transactional
    public boolean deleteBook(long bookId) {
        final Optional<Book> book = bookRepository.getById(bookId);
        book.ifPresent(bookRepository::delete);
        return book.isPresent();
    }
}
