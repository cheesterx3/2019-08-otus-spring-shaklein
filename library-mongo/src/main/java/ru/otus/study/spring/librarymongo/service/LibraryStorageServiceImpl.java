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

        final Genre genre = findOrCreateGenre(genreName);
        return Optional.of(bookRepository.save(new Book(name, author, genre)));
    }

    @Override
    public Optional<Author> addNewAuthor(String name) throws DaoException {
        Objects.requireNonNull(name, "Author name cannot be null");
        if (authorRepository.existsByNameEqualsIgnoreCase(name)) {
            throw new DaoException(String.format("Author with name [%s] already exists", name));
        }
        return Optional.of(authorRepository.save(new Author(name)));
    }

    private Genre findOrCreateGenre(String genreName) {
        final Optional<Genre> genreInfo = genreRepository.findByNameIgnoreCase(genreName);
        return genreInfo.orElseGet(() -> genreRepository.save(new Genre(genreName)));
    }

    @Override
    public boolean deleteBook(String bookId) {
        final Optional<Book> book = bookRepository.findById(bookId);
        book.ifPresent(bookRepository::delete);
        return book.isPresent();
    }

    @Override
    public void deleteGenre(String genreId) throws DaoException {
        if (bookRepository.hasBookWithSingleGenreId(genreId)) {
            throw new DaoException("Unable to remove genre, cause it assigned to book(s) as single genre. Remove genre from book(s) first");
        }
        genreRepository.deleteById(genreId);
    }

    @Override
    public void deleteAuthor(String authorId) throws DaoException {
        if (bookRepository.hasBookWithSingleAuthorId(authorId)) {
            throw new DaoException("Unable to remove author, cause it assigned to book(s) as single author. Remove author from book(s) first");
        }
        authorRepository.deleteById(authorId);
    }

    @Override
    public void removeGenreFromBook(String bookId, String genreId) throws DaoException {
        bookRepository.removeGenreFromBookByBookId(bookId, genreId);
    }

    @Override
    public void addGenreToBook(String bookId, String genreName) throws DaoException {
        checkForBookIsPresentById(bookId);
        if (bookRepository.existsByIdAndAuthorsContains(bookId,genreName)) {
            throw new DaoException(String.format("Genre with name [%s] is already assigned to book", genreName));
        }
        final Genre genre = findOrCreateGenre(genreName);
        bookRepository.addGenreToBook(bookId, genre);
    }

    private void checkForBookIsPresentById(String bookId) throws DaoException {
        if (!bookRepository.existsById(bookId)) {
            throw new DaoException(String.format("Book with id [%s] not found", bookId));
        }
    }

    @Override
    public void removeAuthorFromBook(String bookId, String authorId) throws DaoException {
        checkForBookAndAuthorIdCorrect(bookId, authorId);
        if (bookRepository.existsByIdAndAuthorsContains(bookId, authorId)) {
            bookRepository.removeAuthorFromBookByBookId(bookId, authorId);
        } else {
            throw new DaoException(String.format("Book doesn't contain author with id [%s]", authorId));
        }
    }

    private void checkForBookAndAuthorIdCorrect(String bookId, String authorId) throws DaoException {
        checkForBookIsPresentById(bookId);
        if (!authorRepository.existsById(authorId)) {
            throw new DaoException(String.format("Author with id [%s] not found", authorId));
        }
    }

    @Override
    public void addAuthorToBook(String bookId, String authorId) throws DaoException {
        checkForBookIsPresentById(bookId);
        if (bookRepository.existsByIdAndAuthorsContains(bookId, authorId)) {
            throw new DaoException(String.format("Author with id [%s] is already assigned to book", authorId));
        }
        final Optional<Author> author = authorRepository.findById(authorId);
        author.map(foundAuthor -> {
            bookRepository.addAuthorToBook(bookId, foundAuthor);
            return foundAuthor;
        }).orElseThrow(() -> new DaoException(String.format("Author with id [%s] not found", authorId)));
    }
}
