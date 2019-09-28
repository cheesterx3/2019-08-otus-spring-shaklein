package ru.otus.study.spring.librarydao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.model.BookComment;
import ru.otus.study.spring.librarydao.repository.BookRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryReaderServiceImpl implements LibraryReaderService {
    private final BookRepository bookRepository;

    public LibraryReaderServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
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
        if (book.isPresent()) {
            return new ArrayList<>(book.get().getComments());
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public BookComment commentBook(long bookId, String comment) {
        final Optional<Book> book = bookRepository.getById(bookId);
        return book.map(value -> bookRepository.commentBook(value, comment)).orElse(null);
    }
}
