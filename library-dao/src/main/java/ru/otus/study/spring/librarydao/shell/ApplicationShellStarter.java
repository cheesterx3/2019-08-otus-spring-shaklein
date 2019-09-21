package ru.otus.study.spring.librarydao.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.study.spring.librarydao.model.Author;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.Genre;
import ru.otus.study.spring.librarydao.repository.AuthorRepository;
import ru.otus.study.spring.librarydao.repository.BookRepository;
import ru.otus.study.spring.librarydao.repository.GenreRepository;

import java.util.List;
import java.util.Objects;

@ShellComponent
public class ApplicationShellStarter {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public ApplicationShellStarter(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @ShellMethod(value = "Book list show", key = {"books"})
    public List<Book> showAllBooks() {
        return bookRepository.getAll();
    }

    @ShellMethod(value = "Author list show", key = {"authors"})
    public List<Author> showAllAuthors() {
        return authorRepository.getAll();
    }

    @ShellMethod(value = "Genre list show", key = {"genres"})
    public List<Genre> showAllGenres() {
        return genreRepository.getAll();
    }

    @ShellMethod(value = "Add new book", key = {"add_book", "ab"})
    public void addBook(@ShellOption(help = "book name") String name, @ShellOption(help = "genre id") long genreId, @ShellOption(help = "author id") long authorId) {
        final Genre genre = genreRepository.getById(genreId);
        final Author author = authorRepository.getById(authorId);

        final Book book = bookRepository.insert(name, author, genre);
        System.out.println("New book was added to database: " + book);
    }

    @ShellMethod(value = "Find a book by id", key = {"find_book", "fb"})
    public Book getBookById(@ShellOption(help = "book id") long id) {
        final Book book = bookRepository.getById(id);
        if (Objects.isNull(book)) {
            System.out.println("Book with entered id not found");
        }
        return book;
    }

    @ShellMethod(value = "Delete a book by id", key = {"del_book", "db"})
    public void delBook(@ShellOption(help = "book id") long id) {
        if (bookRepository.deleteById(id)) {
            System.out.println("Book successfully deleted");
        } else {
            System.out.println("Book with entered id not found");
        }
    }

}
