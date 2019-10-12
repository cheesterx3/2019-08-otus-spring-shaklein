package ru.otus.study.spring.libraryspringdata.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.study.spring.libraryspringdata.domain.Book;
import ru.otus.study.spring.libraryspringdata.domain.BookComment;
import ru.otus.study.spring.libraryspringdata.exception.DaoException;
import ru.otus.study.spring.libraryspringdata.repository.BookCommentRepository;
import ru.otus.study.spring.libraryspringdata.repository.BookRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryReaderServiceImpl implements LibraryReaderService {
    private final BookRepository bookRepository;
    private final BookCommentRepository bookCommentRepository;

    public LibraryReaderServiceImpl(BookRepository bookRepository, BookCommentRepository bookCommentRepository) {
        this.bookRepository = bookRepository;
        this.bookCommentRepository = bookCommentRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookComment> getBookComments(long bookId) {
        final Optional<Book> book = bookRepository.findById(bookId);
        return book.map(b -> bookCommentRepository.getAllByBook(book.get()))
                .orElse(Collections.emptyList());
    }

    @Override
    @Transactional(rollbackFor = DaoException.class)
    public Optional<BookComment> commentBook(long bookId, String comment) throws DaoException {
        final Optional<Book> book = bookRepository.findById(bookId);
        return book.map(bookValue
                -> Optional.ofNullable(bookCommentRepository.save(new BookComment(comment, bookValue)))
        ).orElseThrow(() -> new DaoException("Book was not found"));
    }

}
