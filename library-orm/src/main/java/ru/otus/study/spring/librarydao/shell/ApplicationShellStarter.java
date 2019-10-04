package ru.otus.study.spring.librarydao.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.study.spring.librarydao.exception.DaoException;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.BookComment;
import ru.otus.study.spring.librarydao.repository.GenreRepository;
import ru.otus.study.spring.librarydao.service.LibraryReaderService;
import ru.otus.study.spring.librarydao.service.LibraryStorageService;

import java.util.List;
import java.util.Optional;

@ShellComponent
public class ApplicationShellStarter {
    private final LibraryReaderService libraryReaderService;
    private final LibraryStorageService libraryStorageService;
    private final GenreRepository genreRepository;

    public ApplicationShellStarter(LibraryReaderService libraryReaderService, LibraryStorageService libraryStorageService, GenreRepository genreRepository) {
        this.libraryReaderService = libraryReaderService;
        this.libraryStorageService = libraryStorageService;
        this.genreRepository = genreRepository;
    }

    @ShellMethod(value = "Book list show", key = {"books"})
    public List<Book> showAllBooks() {
        return libraryReaderService.getAllBooks();
    }

    @ShellMethod(value = "Book comments", key = {"comments"})
    public List<BookComment> showBookComments(@ShellOption(help = "book id") long bookId) {
        final List<BookComment> comments = libraryReaderService.getBookComments(bookId);
        if (comments.isEmpty()) {
            System.out.println("This book has no comments");
        }
        return comments;
    }

    @ShellMethod(value = "Add new book", key = {"add_book", "ab"})
    public void addBook(@ShellOption(help = "book name") String name, @ShellOption(help = "genre name") String genreName, @ShellOption(help = "author id") long authorId) {
        final Optional<Book> newBookResult;
        try {
            newBookResult = libraryStorageService.addNewBook(name, authorId, genreName);
            newBookResult.ifPresent(book -> System.out.println("New book was added. " + book));
        } catch (DaoException e) {
            System.out.println("Book was not added cause of exception. " + e.getMessage());
        }
    }

    @ShellMethod(value = "Add new comment to book", key = {"comment_book", "cb"})
    public BookComment commentBook(@ShellOption(help = "book id") long bookId, @ShellOption(help = "comment text") String comment) {
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

    @ShellMethod(value = "Find a book by id", key = {"find_book", "fb"})
    public Book getBookById(@ShellOption(help = "book id") long id) {
        final Optional<Book> book = libraryReaderService.getBookById(id);
        return book.orElseGet(()->{
            System.out.println("Book with entered id not found");
            return null;
        });
    }

    @ShellMethod(value = "Delete a book by id", key = {"del_book", "db"})
    public void delBook(@ShellOption(help = "book id") long id) {
        if (libraryStorageService.deleteBook(id)) {
            System.out.println("Book successfully deleted");
        } else {
            System.out.println("Book with entered id not found");
        }
    }

}
