package ru.otus.study.spring.librarydao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.study.spring.librarydao.helper.GenericDaoResult;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.BookComment;
import ru.otus.study.spring.librarydao.repository.BookCommentRepository;
import ru.otus.study.spring.librarydao.repository.BookRepository;

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
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getBookById(long id) {
        return bookRepository.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookComment> getBookComments(long bookId) {
        final Optional<Book> book = bookRepository.getById(bookId);
        return book.map(b -> bookCommentRepository.getBookComments(book.get()))
                .orElse(Collections.emptyList());
    }

    @Override
    @Transactional
    public GenericDaoResult<BookComment> commentBook(long bookId, String comment) {
        final Optional<Book> book = bookRepository.getById(bookId);
        return book.map(value -> GenericDaoResult.createResult(bookCommentRepository.commentBook(value, comment)))
                .orElse(GenericDaoResult.createError("Book was not found"));
    }
}
