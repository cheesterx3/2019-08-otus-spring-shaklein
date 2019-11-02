package ru.otus.study.spring.libraryweb.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.domain.Genre;
import ru.otus.study.spring.libraryweb.exception.DaoException;
import ru.otus.study.spring.libraryweb.exception.NotFoundException;
import ru.otus.study.spring.libraryweb.repository.AuthorRepository;
import ru.otus.study.spring.libraryweb.repository.BookRepository;
import ru.otus.study.spring.libraryweb.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LibraryStorageServiceImpl implements LibraryStorageService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> addNewBook(String name, String authorId, String genreName) throws DaoException, NotFoundException {
        if (Objects.isNull(genreName)) {
            throw new DaoException("Genre name cannot be null");
        }
        final Author author = getAuthor(authorId);
        final Genre genre = findOrCreateGenre(genreName);
        return Optional.of(bookRepository.save(new Book(name, author, genre)));
    }

    private Author getAuthor(String authorId) throws NotFoundException {
        final Optional<Author> authorOptional = authorRepository.findById(authorId);
        return authorOptional.orElseThrow(() -> new NotFoundException(String.format("Author with id [%s] not found", authorId)));
    }

    @Override
    public Optional<Book> addNewBookWithGenreId(String name, String authorId, String genreId) throws DaoException, NotFoundException {
        if (Objects.isNull(genreId)) {
            throw new DaoException("Genre id cannot be null");
        }
        final Author author = getAuthor(authorId);
        final Optional<Genre> genreOptional = genreRepository.findById(genreId);
        final Genre genre = genreOptional.orElseThrow(() -> new NotFoundException("Genre not found"));
        return Optional.of(bookRepository.save(new Book(name, author, genre)));
    }

    @Override
    public Optional<Book> addNewBook(String name, List<String> authorId, List<String> genreId) throws DaoException {
        final List<Genre> genres = getGenresWithIds(genreId);
        final List<Author> authors = getAuthorsWithIds(authorId);
        if (!genres.isEmpty() && !authors.isEmpty()) {
            return Optional.of(bookRepository.save(new Book(name, authors, genres)));
        }
        throw new DaoException(genres.isEmpty()
                ? "Genres shouldn't be empty"
                : "Authors shouldn't be empty");
    }

    @Override
    public Optional<Book> saveBook(String bookId, String name, List<String> authorId, List<String> genreId) throws DaoException, NotFoundException {
        final List<Genre> genres = getGenresWithIds(genreId);
        final List<Author> authors = getAuthorsWithIds(authorId);
        if (!genres.isEmpty() && !authors.isEmpty()) {
            final Optional<Book> foundBook = bookRepository.findById(bookId);
            return foundBook.map(book -> {
                book.setGenres(genres);
                book.setAuthors(authors);
                book.setName(name);
                return Optional.of(bookRepository.save(book));
            }).orElseThrow(() -> new NotFoundException(String.format("Book with id [%s] not found", bookId)));
        }
        throw new DaoException(genres.isEmpty()
                ? "Genres shouldn't be empty"
                : "Authors shouldn't be empty");
    }

    private List<Genre> getGenresWithIds(List<String> genresId) throws DaoException {
        if (genresId == null) {
            throw new DaoException("Genres list should not be empty");
        }
        final List<Genre> genres = new ArrayList<>();
        genreRepository.findAllById(genresId).forEach(genres::add);
        return genres;
    }

    private List<Author> getAuthorsWithIds(List<String> authorsId) throws DaoException {
        if (authorsId == null) {
            throw new DaoException("Authors list should not be empty");
        }
        final List<Author> authors = new ArrayList<>();
        authorRepository.findAllById(authorsId).forEach(authors::add);
        return authors;
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
    public void updateBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public boolean isBookExistsById(String bookId) {
        return bookRepository.existsById(bookId);
    }

    @Override
    public boolean isAuthorExistsById(String authorId) {
        return authorRepository.existsById(authorId);
    }

    @Override
    public boolean isGenreExistsById(String genreId) {
        return genreRepository.existsById(genreId);
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
    public void removeGenreFromBook(String bookId, String genreId) throws DaoException, NotFoundException {
        bookRepository.removeGenreFromBookByBookId(bookId, genreId);
    }

    @Override
    public void addGenreToBook(String bookId, String genreName) throws DaoException, NotFoundException {
        checkForBookIsPresentById(bookId);
        if (bookRepository.existsByIdAndGenresContains(bookId, genreName)) {
            throw new DaoException(String.format("Genre with name [%s] is already assigned to book", genreName));
        }
        final Genre genre = findOrCreateGenre(genreName);
        bookRepository.addGenreToBook(bookId, genre);
    }

    @Override
    public void addGenreWithIdToBook(String bookId, String genreId) throws DaoException, NotFoundException {
        checkForBookIsPresentById(bookId);
        if (bookRepository.existsByIdAndGenresContainsById(bookId, genreId)) {
            throw new DaoException(String.format("Genre with id [%s] is already assigned to book", genreId));
        }
        genreRepository.findById(genreId).map(genre -> {
            bookRepository.addGenreToBook(bookId, genre);
            return genre;
        }).orElseThrow(() -> new NotFoundException("Genre not found"));
    }

    private void checkForBookIsPresentById(String bookId) throws NotFoundException {
        if (!bookRepository.existsById(bookId)) {
            throw new NotFoundException(String.format("Book with id [%s] not found", bookId));
        }
    }

    @Override
    public void removeAuthorFromBook(String bookId, String authorId) throws DaoException, NotFoundException {
        checkForBookAndAuthorIdCorrect(bookId, authorId);
        if (bookRepository.existsByIdAndAuthorsContains(bookId, authorId)) {
            bookRepository.removeAuthorFromBookByBookId(bookId, authorId);
        } else {
            throw new NotFoundException(String.format("Book doesn't contain author with id [%s]", authorId));
        }
    }

    private void checkForBookAndAuthorIdCorrect(String bookId, String authorId) throws NotFoundException {
        checkForBookIsPresentById(bookId);
        if (!authorRepository.existsById(authorId)) {
            throw new NotFoundException(String.format("Author with id [%s] not found", authorId));
        }
    }

    @Override
    public void addAuthorToBook(String bookId, String authorId) throws DaoException, NotFoundException {
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
        }).orElseThrow(() -> new NotFoundException(String.format("Author with id [%s] not found", authorId)));
    }
}
