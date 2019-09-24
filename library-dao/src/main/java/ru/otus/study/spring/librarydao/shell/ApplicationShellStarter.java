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
import java.util.Optional;

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
        final Optional<Genre> genre = genreRepository.getById(genreId);
        final Optional<Author> author = authorRepository.getById(authorId);
        if (genre.isPresent() && author.isPresent()) {
            final Book book = bookRepository.insert(name, author.get(), genre.get());
            System.out.println("New book was added to database: " + book);
        } else {
            System.out.println("Couldn't add new book to database. Cause errors: ");
            if (!genre.isPresent()) {
                System.out.println("\tGenre was not found");
            }
            if (!author.isPresent()) {
                System.out.println("\tAuthor was not found");
            }
        }
    }

    @ShellMethod(value = "Find a book by id", key = {"find_book", "fb"})
    public Book getBookById(@ShellOption(help = "book id") long id) {
        final Optional<Book> book = bookRepository.getById(id);
        if (!book.isPresent()) {
            System.out.println("Book with entered id not found");
        }
        return book.orElse(null);
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
