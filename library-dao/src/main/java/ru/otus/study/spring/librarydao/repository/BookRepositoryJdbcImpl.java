package ru.otus.study.spring.librarydao.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.study.spring.librarydao.model.Author;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.Genre;
import ru.otus.study.spring.librarydao.repository.mapper.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
public class BookRepositoryJdbcImpl implements BookRepository {
    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookRepositoryJdbcImpl(NamedParameterJdbcOperations namedParameterJdbcOperations, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from book", Integer.class);
    }

    @Override
    public Optional<Book> getById(long id) {
        Book book;
        final Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            book = namedParameterJdbcOperations.queryForObject("select * from book where id = :id", params, new BookMapper());
            if (Objects.nonNull(book)) {
                fillBookDetails(id, book);
            }
        } catch (EmptyResultDataAccessException e) {
            book = null;
        }
        return Optional.ofNullable(book);
    }

    private void fillBookDetails(long id, Book book) {
        final List<Author> authorsForBook = getAuthorsForBook(id);
        final List<Genre> genresForBook = getGenresForBook(id);
        book.getAuthors().addAll(authorsForBook);
        book.getGenres().addAll(genresForBook);
    }

    @Override
    public List<Book> getAll() {
        final List<Book> bookList = namedParameterJdbcOperations.query("select * from book ", new BookMapper());
        final Map<Long, List<Author>> bookAuthorMap = getAuthorsForBooks();
        final Map<Long, List<Genre>> bookGenreMap = getGenresForBooks();

        bookList.forEach(book -> {
            book.getGenres().addAll(bookGenreMap.get(book.getId()));
            book.getAuthors().addAll(bookAuthorMap.get(book.getId()));
        });

        return bookList;
    }

    private Map<Long, List<Genre>> getGenresForBooks() {
        return namedParameterJdbcOperations.query("select genre.*,book_id from ref_book_genre rba,genre where genre.id=rba.genre_id", new BookGenresExtractor());
    }

    private Map<Long, List<Author>> getAuthorsForBooks() {
        return namedParameterJdbcOperations.query("select author.*,book_id from ref_book_author rba,author where author.id=rba.author_id", new BookAuthorsExtractor());
    }

    private List<Author> getAuthorsForBook(long bookId) {
        Map<String, Object> params = Collections.singletonMap("id", bookId);
        return namedParameterJdbcOperations.query("select author.* from ref_book_author rba,author where book_id = :id and author.id=rba.author_id", params, new AuthorMapper());
    }

    private List<Genre> getGenresForBook(long bookId) {
        Map<String, Object> params = Collections.singletonMap("id", bookId);
        return namedParameterJdbcOperations.query("select genre.* from ref_book_genre rbg,genre where book_id = :id and genre.id=rbg.genre_id", params, new GenreMapper());
    }

    @Override
    public boolean deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.update("delete from book where id = :id", params) == 1;
    }

    @Override
    public Book insert(String bookName, Author author, Genre genre) {
        Objects.requireNonNull(author, "Author cannot be null");
        Objects.requireNonNull(genre, "Genre cannot be null");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            String[] params = new String[]{bookName};
            PreparedStatement ps = connection.prepareStatement("insert into book (`name`) values (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, params[0]);
            return ps;
        }, keyHolder);
        long bookId = (long) keyHolder.getKey();
        final Book book = new Book(bookId, bookName);

        updateBookGenreAndAuthor(author, genre, book);

        return book;
    }

    private void updateBookGenreAndAuthor(Author author, Genre genre, Book book) {
        final long bookId = book.getId();
        updateBookAuthor(bookId, author.getId());
        updateBookGenre(bookId, genre.getId());

        book.getAuthors().add(author);
        book.getGenres().add(genre);
    }

    private void updateBookGenre(long bookId, long genreId) {
        Map<String, Object> params = new HashMap<>();
        params.put("bookId", bookId);
        params.put("genreId", genreId);
        namedParameterJdbcOperations.update("insert into ref_book_genre (book_id,genre_id) values(:bookId,:genreId)", params);
    }

    private void updateBookAuthor(long bookId, long authorId) {
        Map<String, Object> params = new HashMap<>();
        params.put("bookId", bookId);
        params.put("authorId", authorId);
        namedParameterJdbcOperations.update("insert into ref_book_author (book_id,author_id) values(:bookId,:authorId)", params);
    }

}
