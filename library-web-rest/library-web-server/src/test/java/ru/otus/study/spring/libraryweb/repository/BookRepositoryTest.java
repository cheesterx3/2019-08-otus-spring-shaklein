package ru.otus.study.spring.libraryweb.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.domain.BookComment;
import ru.otus.study.spring.libraryweb.domain.Genre;
import ru.otus.study.spring.libraryweb.exception.DaoException;
import ru.otus.study.spring.libraryweb.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий работы с книгами должен ")
@ComponentScan("ru.otus.study.spring.libraryweb.events")
class BookRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookCommentRepository bookCommentRepository;

    @DisplayName("при удалении книги удалить все ссылающиеся на неё комментарии")
    @Test
    void shouldRemoveBookCommentsOnBookDeletion() {
        final List<Book> allBooks = bookRepository.findAll();
        final Book book = allBooks.get(0);

        bookRepository.delete(book);
        final List<BookComment> comments = bookCommentRepository.findAllByBook_Id(book.getId());
        assertThat(comments).isEmpty();
    }

    @DisplayName("при удалении единственного жанра из книга должен выкидывать исключение")
    @Test
    void shouldThrowExceptionOnSingleGenreRemoveFromBook() {
        final List<Book> allBooks = bookRepository.findAll();
        final Book book = allBooks.get(1);
        final Genre genre = book.getGenres().get(0);

        assertThrows(DaoException.class, () -> bookRepository.removeGenreFromBookByBookId(book.getId(), genre.getId()));
    }

    @DisplayName("корректно удалять жанр у книги, если он не единственный")
    @Test
    void shouldCorrectGenreRemoveFromBook() throws DaoException, NotFoundException {
        final List<Book> allBooks = bookRepository.findAll();
        final Book book = allBooks.get(0);
        final Genre genre = book.getGenres().get(0);
        final int currentGenreCount = book.getGenres().size();
        bookRepository.removeGenreFromBookByBookId(book.getId(), genre.getId());

        final Optional<Book> actualBook = bookRepository.findById(book.getId());
        assertThat(actualBook)
                .isPresent()
                .get()
                .matches(b -> b.getGenres().stream().noneMatch(g -> g.getId().equals(genre.getId())))
                .matches(b -> b.getGenres().size() == currentGenreCount - 1);
    }

    @DisplayName("при удалении единственного автора из книга должен выкидывать исключение")
    @Test
    void shouldThrowExceptionOnSingleAuthorRemoveFromBook() {
        final List<Book> allBooks = bookRepository.findAll();
        final Book book = allBooks.get(0);
        final Author author = book.getAuthors().get(0);

        assertThrows(DaoException.class, () -> bookRepository.removeAuthorFromBookByBookId(book.getId(), author.getId()));
    }

    @DisplayName("корректно удалять автора у книги, если он не единственный")
    @Test
    void shouldCorrectAuthorRemoveFromBook() throws DaoException {
        final List<Book> allBooks = bookRepository.findAll();
        final Book book = allBooks.get(2);
        final Author author = book.getAuthors().get(0);
        final int currentAuthorsCount = book.getAuthors().size();
        bookRepository.removeAuthorFromBookByBookId(book.getId(), author.getId());

        final Optional<Book> actualBook = bookRepository.findById(book.getId());
        assertThat(actualBook)
                .isPresent()
                .get()
                .matches(b -> b.getAuthors().stream().noneMatch(a -> a.getId().equals(author.getId())))
                .matches(b -> b.getAuthors().size() == currentAuthorsCount - 1);
    }

    @DisplayName("получать список жанров для книги по запросу")
    @Test
    void shouldSelectGenresForBook() {
        final List<Book> allBooks = bookRepository.findAll();
        final Book book = allBooks.get(0);
        final List<Genre> genresForBookId = bookRepository.findGenresForBookId(book.getId());
        assertThat(genresForBookId)
                .isNotEmpty()
                .isEqualTo(book.getGenres());
    }

    @DisplayName("возвращать true при проверке на наличие книг с единственным жанром по идентификтаору, если таковые имеются")
    @Test
    void shouldReturnTrueIfContainsBookWithGenre() {
        final List<Genre> genreList = genreRepository.findAll();
        final Genre genre = genreList.get(1);
        final boolean exists = bookRepository.hasBookWithSingleGenreId(genre.getId());
        assertThat(exists).isTrue();
    }

    @DisplayName("возвращать false при проверке на наличие книг с единственным жанром по идентификтаору, если таковых нет")
    @Test
    void shouldReturnFalseIfNotContainsBookWithGenre() {
        final List<Genre> genreList = genreRepository.findAll();
        final Genre genre = genreList.get(3);
        final boolean exists = bookRepository.hasBookWithSingleGenreId(genre.getId());
        assertThat(exists).isFalse();
    }

    @DisplayName("корректно добавлять автора к книге")
    @Test
    void shouldCorrectAuthorAddToBook() throws DaoException {
        final List<Book> allBooks = bookRepository.findAll();
        final List<Author> authorList = authorRepository.findAll();
        final Book book = allBooks.get(0);
        final Author author = authorList.get(2);
        final int currentAuthorsCount = book.getAuthors().size();
        bookRepository.addAuthorToBook(book.getId(), author);

        final Optional<Book> actualBook = bookRepository.findById(book.getId());
        assertThat(actualBook)
                .isPresent()
                .get()
                .matches(b -> b.getAuthors().stream().anyMatch(a -> a.getId().equals(author.getId())))
                .matches(b -> b.getAuthors().size() == currentAuthorsCount + 1);
    }

    @DisplayName("корректно добавлять жанр к книге")
    @Test
    void shouldCorrectGenreAddToBook() throws DaoException {
        final List<Book> allBooks = bookRepository.findAll();
        final List<Genre> genreList = genreRepository.findAll();
        final Book book = allBooks.get(0);
        final Genre genre = genreList.get(3);

        final int currentGenreCount = book.getGenres().size();
        bookRepository.addGenreToBook(book.getId(), genre);

        final Optional<Book> actualBook = bookRepository.findById(book.getId());
        assertThat(actualBook)
                .isPresent()
                .get()
                .matches(b -> b.getGenres().stream().anyMatch(g -> g.getId().equals(genre.getId())))
                .matches(b -> b.getGenres().size() == currentGenreCount + 1);
    }

    @DisplayName("выкидывать исключение при попытке привязать null-автора к книге")
    @Test
    void shouldThrowExceptionIfExistingAuthorAddToBook() {
        final List<Book> allBooks = bookRepository.findAll();
        final Book book = allBooks.get(0);
        assertThrows(NullPointerException.class, () -> bookRepository.addAuthorToBook(book.getId(), null));
    }

    @DisplayName("выкидывать исключение при попытке привязать null-жанр к книге")
    @Test
    void shouldThrowExceptionIfExistingGenreAddToBook() {
        final List<Book> allBooks = bookRepository.findAll();
        final Book book = allBooks.get(0);
        assertThrows(NullPointerException.class, () -> bookRepository.addGenreToBook(book.getId(), null));
    }
}