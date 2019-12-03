package ru.otus.study.spring.librarymvc.service;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.study.spring.librarymvc.domain.Author;
import ru.otus.study.spring.librarymvc.domain.Book;
import ru.otus.study.spring.librarymvc.domain.Genre;
import ru.otus.study.spring.librarymvc.exception.DaoException;
import ru.otus.study.spring.librarymvc.repository.AuthorRepository;
import ru.otus.study.spring.librarymvc.repository.BookRepository;
import ru.otus.study.spring.librarymvc.repository.GenreRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LibraryStorageServiceImpl implements LibraryStorageService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final LibraryPermissionAttachService permissionAttachService;

    @Override
    @Transactional
    public Optional<Book> addNewBook(String name, String authorId, String genreName) throws DaoException {
        if (Objects.isNull(genreName)) {
            throw new DaoException("Genre name cannot be null");
        }
        final Author author = getAuthor(authorId);
        final Genre genre = findOrCreateGenre(genreName);
        return Optional.of(bookRepository.save(new Book(name, author, genre)))
                .map(permissionAttachService::addDefaultPermissionsForBook);
    }

    private Author getAuthor(String authorId) throws DaoException {
        final Optional<Author> authorOptional = authorRepository.findById(authorId);
        return authorOptional.orElseThrow(() -> new DaoException("Author not found"));
    }

    @Override
    @Transactional
    public Optional<Book> addNewBookWithGenreId(String name, String authorId, String genreId) throws DaoException {
        if (Objects.isNull(genreId)) {
            throw new DaoException("Genre name cannot be null");
        }
        final Author author = getAuthor(authorId);
        final Optional<Genre> genreOptional = genreRepository.findById(genreId);
        final Genre genre = genreOptional.orElseThrow(() -> new DaoException("Genre not found"));
        return Optional.of(bookRepository.save(new Book(name, author, genre)))
                .map(permissionAttachService::addDefaultPermissionsForBook);
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
    @PreAuthorize(value = "hasPermission(#book,'ADMINISTRATION')")
    public void updateBook(Book book) {
        bookRepository.save(book);
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
        if (bookRepository.existsByIdAndGenresContains(bookId, genreName)) {
            throw new DaoException(String.format("Genre with name [%s] is already assigned to book", genreName));
        }
        final Genre genre = findOrCreateGenre(genreName);
        bookRepository.addGenreToBook(bookId, genre);
    }

    @Override
    public void addGenreWithIdToBook(String bookId, String genreId) throws DaoException {
        checkForBookIsPresentById(bookId);
        if (bookRepository.existsByIdAndGenresContainsById(bookId, genreId)) {
            throw new DaoException(String.format("Genre with id [%s] is already assigned to book", genreId));
        }
        genreRepository.findById(genreId).map(genre -> {
            bookRepository.addGenreToBook(bookId, genre);
            return genre;
        }).orElseThrow(() -> new DaoException("Genre not found"));
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
        if (Objects.isNull(authorId)) {
            throw new DaoException("Author cannot be null");
        }
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
