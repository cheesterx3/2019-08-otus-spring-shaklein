package ru.otus.study.spring.libraryweb.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.libraryweb.domain.Author;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.domain.BookComment;
import ru.otus.study.spring.libraryweb.domain.Genre;
import ru.otus.study.spring.libraryweb.exception.NotFoundException;
import ru.otus.study.spring.libraryweb.repository.AuthorRepository;
import ru.otus.study.spring.libraryweb.repository.BookCommentRepository;
import ru.otus.study.spring.libraryweb.repository.BookRepository;
import ru.otus.study.spring.libraryweb.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LibraryReaderServiceImpl implements LibraryReaderService {
    private final BookRepository bookRepository;
    private final BookCommentRepository commentRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public List<Genre> findAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public List<Book> getAllBooksSortedByName() {
        return bookRepository.findAll(Sort.by(Sort.Order.asc("name")));
    }

    @Override
    public List<Book> getBooksByNameLike(String name) {
        return bookRepository.findAllByNameLike(name);
    }

    @Override
    public Optional<Book> findBookById(String bookId) {
        return bookRepository.findById(bookId);
    }

    @Override
    public List<BookComment> getBookComments(String bookId) {
        return commentRepository.findAllByBook_Id(bookId);
    }

    @Override
    public Optional<BookComment> commentBook(String bookId, String comment) throws NotFoundException {
        final Optional<Book> book = bookRepository.findById(bookId);
        return book.map(bookValue
                -> Optional.of(commentRepository.save(new BookComment(comment, bookValue)))
        ).orElseThrow(() -> new NotFoundException("Book was not found"));
    }
}
