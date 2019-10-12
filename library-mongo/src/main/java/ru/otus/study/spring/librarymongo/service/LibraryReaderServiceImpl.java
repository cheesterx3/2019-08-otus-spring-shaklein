package ru.otus.study.spring.librarymongo.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.librarymongo.domain.Book;
import ru.otus.study.spring.librarymongo.domain.BookComment;
import ru.otus.study.spring.librarymongo.exception.DaoException;
import ru.otus.study.spring.librarymongo.repository.BookCommentRepository;
import ru.otus.study.spring.librarymongo.repository.BookRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LibraryReaderServiceImpl implements LibraryReaderService {
    private final BookRepository bookRepository;
    private final BookCommentRepository commentRepository;

    @Override
    public List<Book> getAllBooksSortedByName() {
        return bookRepository.findAll(Sort.by(Sort.Order.asc("name")));
    }

    @Override
    public List<Book> getBooksByNameLike(String name) {
        return bookRepository.findAllByNameLike(name);
    }

    @Override
    public List<BookComment> getBookComments(String bookId){
        return commentRepository.findAllByBook_Id(bookId);
    }

    @Override
    public Optional<BookComment> commentBook(String bookId, String comment) throws DaoException {
        final Optional<Book> book = bookRepository.findById(bookId);
        return book.map(bookValue
                -> Optional.of(commentRepository.save(new BookComment(comment, bookValue)))
        ).orElseThrow(() -> new DaoException("Book was not found"));
    }
}
