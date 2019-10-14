package ru.otus.study.spring.librarymongo.shell;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.study.spring.librarymongo.domain.Author;
import ru.otus.study.spring.librarymongo.domain.Book;
import ru.otus.study.spring.librarymongo.exception.DaoException;
import ru.otus.study.spring.librarymongo.service.LibraryStorageService;

import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class ShellWorkerForBookEditing {
    private final LibraryStorageService libraryStorageService;

    @ShellMethod(value = "Add new book", key = {"add_book", "ab"})
    public void addBook(@ShellOption(help = "book name") String name, @ShellOption(help = "genre name") String genreName, @ShellOption(help = "author id") String authorId) {
        final Optional<Book> newBookResult;
        try {
            newBookResult = libraryStorageService.addNewBook(name, authorId, genreName);
            newBookResult.ifPresent(book -> System.out.println("New book was added. " + book));
        } catch (DaoException e) {
            System.out.println("Book was not added cause of exception. " + e.getMessage());
        }
    }

    @ShellMethod(value = "Add new author", key = {"add_author", "aa"})
    public void addAuthor(@ShellOption(help = "author name") String name) {
        final Optional<Author> optionalAuthor;
        try {
            optionalAuthor = libraryStorageService.addNewAuthor(name);
            optionalAuthor.ifPresent(author -> System.out.println("New author was added. " + author));
        } catch (DaoException e) {
            System.out.println("Author was not added cause of exception. " + e.getMessage());
        }
    }

    @ShellMethod(value = "Remove book genre", key = {"del_book_genre", "dbg"})
    public void removeGenreFromBook(@ShellOption(help = "book id") String bookId, @ShellOption(help = "genre id") String genreId) {
        try {
            libraryStorageService.removeGenreFromBook(bookId, genreId);
        } catch (DaoException e) {
            System.out.println(String.format("Unable to remove genre from book cause of exception. %s", e.getMessage()));
        }
    }

    @ShellMethod(value = "Delete a book by id", key = {"del_book", "db"})
    public void delBook(@ShellOption(help = "book id") String bookId) {
        if (libraryStorageService.deleteBook(bookId)) {
            System.out.println("Book successfully deleted");
        } else {
            System.out.println("Book with entered id not found");
        }
    }

    @ShellMethod(value = "Delete an author from book", key = {"del_author_book", "dab"})
    public void delAuthorFromBook(@ShellOption(help = "book id") String bookId, @ShellOption(help = "author id") String authorId) {
        try {
            libraryStorageService.removeAuthorFromBook(bookId, authorId);
        } catch (DaoException e) {
            System.out.println(String.format("Unable to remove author from book cause of exception. %s", e.getMessage()));
        }
    }

    @ShellMethod(value = "Assign an author to book", key = {"add_author_book", "aab"})
    public void addAuthorToBook(@ShellOption(help = "book id") String bookId, @ShellOption(help = "author id") String authorId) {
        try {
            libraryStorageService.addAuthorToBook(bookId, authorId);
        } catch (DaoException e) {
            System.out.println(String.format("Unable to assign author to book cause of exception. %s", e.getMessage()));
        }
    }

    @ShellMethod(value = "Assign a genre to book", key = {"add_genre_book", "agb"})
    public void addGenreToBook(@ShellOption(help = "book id") String bookId, @ShellOption(help = "genre name") String genreName) {
        try {
            libraryStorageService.addGenreToBook(bookId, genreName);
        } catch (DaoException e) {
            System.out.println(String.format("Unable to assign genre to book cause of exception. %s", e.getMessage()));
        }
    }

    @ShellMethod(value = "Delete a genre by id", key = {"del_genre", "dg"})
    public void delGenre(@ShellOption(help = "genre id") String genreId) {
        try {
            libraryStorageService.deleteGenre(genreId);
        } catch (DaoException e) {
            System.out.println(String.format("Unable to remove genre cause of exception. %s", e.getMessage()));
        }
    }

    @ShellMethod(value = "Delete an author by id", key = {"del_author", "da"})
    public void delAuthor(@ShellOption(help = "author id") String authorId) {
        try {
            libraryStorageService.deleteAuthor(authorId);
        } catch (DaoException e) {
            System.out.println(String.format("Unable to remove author cause of exception. %s", e.getMessage()));
        }
    }
}
