package ru.otus.study.spring.librarydao.repository;

import org.springframework.stereotype.Repository;
import ru.otus.study.spring.librarydao.helper.GenericDaoResult;
import ru.otus.study.spring.librarydao.model.Author;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.BookComment;
import ru.otus.study.spring.librarydao.model.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class BookRepositoryJpaImpl implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    private final GenreRepository genreRepository;

    public BookRepositoryJpaImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }


    @Override
    public Optional<Book> getById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> getAll() {
        return em.createQuery("select b from Book b", Book.class).getResultList();
    }

    @Override
    public boolean deleteById(long id) {
        final Optional<Book> book = getById(id);
        book.ifPresent(b -> em.remove(b));
        return book.isPresent();
    }

    @Override
    public GenericDaoResult<Book> insert(String bookName, Author author, String genreName) {
        if (Objects.isNull(genreName)) {
            return GenericDaoResult.createError("Genre name cannot be null");
        }
        final GenericDaoResult<Genre> createGenre = findOrCreateGenre(genreName);
        if (createGenre.getResult().isPresent()) {
            final Genre genre = createGenre.getResult().get();
            final Book book = new Book(bookName);
            em.persist(book);
            saveBookGenreAndAuthor(author, genre, book);
            return GenericDaoResult.createResult(book);
        } else {
            return GenericDaoResult.createError(createGenre.getError());
        }
    }

    private GenericDaoResult<Genre> findOrCreateGenre(String genreName) {
        final GenericDaoResult<Genre> currentGenre = genreRepository.getByName(genreName);

        if (currentGenre.getResult().isPresent()) {
            return GenericDaoResult.createResult(currentGenre.getResult().get());
        } else {
            final GenericDaoResult<Genre> insertResult = genreRepository.insert(genreName);
            if (!insertResult.getResult().isPresent()) {
                return GenericDaoResult.createError(insertResult.getError());
            }
            return GenericDaoResult.createResult(insertResult.getResult().get());
        }
    }

    private void saveBookGenreAndAuthor(Author author, Genre genre, Book book) {
        book.getGenres().add(genre);
        book.getAuthors().add(author);
        em.merge(book);
    }

    @Override
    public BookComment commentBook(Book book, String comment) {
        final BookComment bookComment = new BookComment(comment);
        book.getComments().add(bookComment);
        em.merge(book);
        return bookComment;
    }
}
