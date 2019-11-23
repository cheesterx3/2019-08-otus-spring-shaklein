package ru.otus.study.spring.librarymvc.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.librarymvc.domain.*;
import ru.otus.study.spring.librarymvc.exception.DaoException;
import ru.otus.study.spring.librarymvc.exception.NotFoundException;
import ru.otus.study.spring.librarymvc.repository.AuthorRepository;
import ru.otus.study.spring.librarymvc.repository.BookCommentRepository;
import ru.otus.study.spring.librarymvc.repository.BookRepository;
import ru.otus.study.spring.librarymvc.repository.GenreRepository;
import ru.otus.study.spring.librarymvc.security.CustomUserPrincipal;

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
        return commentRepository.findAllByBookIdOrderByTimeDesc(bookId);
    }

    @Override
    public Optional<BookComment> commentBook(String bookId, String comment, User user) throws NotFoundException {
        final Optional<Book> book = bookRepository.findById(bookId);
        return book.map(bookValue
                -> Optional.of(commentRepository.save(new BookComment(comment, bookValue,user)))
        ).orElseThrow(() -> new NotFoundException("Book was not found"));
    }
}
