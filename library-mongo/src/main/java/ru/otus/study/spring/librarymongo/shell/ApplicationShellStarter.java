package ru.otus.study.spring.librarymongo.shell;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.study.spring.librarymongo.domain.Author;
import ru.otus.study.spring.librarymongo.domain.Book;
import ru.otus.study.spring.librarymongo.domain.BookComment;
import ru.otus.study.spring.librarymongo.exception.DaoException;
import ru.otus.study.spring.librarymongo.service.AuthorService;
import ru.otus.study.spring.librarymongo.service.LibraryReaderService;
import ru.otus.study.spring.librarymongo.service.LibraryStorageService;

import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class ApplicationShellStarter {
    private final LibraryReaderService libraryReaderService;
    private final LibraryStorageService libraryStorageService;
    private final AuthorService authorService;

    @ShellMethod(value = "Book list show", key = {"books"})
    public List<Book> showAllBooks() {
        return libraryReaderService.getAllBooksSortedByName();
    }

    @ShellMethod(value = "Authors list show", key = {"authors"})
    public List<Author> showAllAuthors() {
        return authorService.findAllAuthors();
    }

    @ShellMethod(value = "Book comments", key = {"comments"})
    public List<BookComment> showBookComments(@ShellOption(help = "book id") String bookId) {
        final List<BookComment> comments = libraryReaderService.getBookComments(bookId);
        if (comments.isEmpty()) {
            System.out.println("This book has no comments");
        }
        return comments;
    }

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

    @ShellMethod(value = "Add new comment to book", key = {"comment_book", "cb"})
    public BookComment commentBook(@ShellOption(help = "book number") String bookId, @ShellOption(help = "comment text") String comment) {
        try {
            final Optional<BookComment> bookComment = libraryReaderService.commentBook(bookId, comment);
            return bookComment.orElseGet(() -> {
                System.out.println("Book comment was not added");
                return null;
            });
        } catch (DaoException e) {
            System.out.println("Book comment adding error. " + e.getMessage());
        }
        return null;
    }

    @ShellMethod(value = "Find books by name like", key = {"find_book", "fb"})
    public List<Book> getBooksByName(@ShellOption(help = "book name") String name) {
        return libraryReaderService.getBooksByNameLike(name);
    }

    @ShellMethod(value = "Delete a book by id", key = {"del_book", "db"})
    public void delBook(@ShellOption(help = "book id") String bookId) {
        if (libraryStorageService.deleteBook(bookId)) {
            System.out.println("Book successfully deleted");
        } else {
            System.out.println("Book with entered id not found");
        }
    }
}
