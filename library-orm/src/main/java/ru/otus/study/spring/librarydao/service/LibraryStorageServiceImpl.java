package ru.otus.study.spring.librarydao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.study.spring.librarydao.helper.GenericDaoResult;
import ru.otus.study.spring.librarydao.model.Author;
import ru.otus.study.spring.librarydao.model.Book;
import ru.otus.study.spring.librarydao.repository.AuthorRepository;
import ru.otus.study.spring.librarydao.repository.BookRepository;

import java.util.Optional;

@Service
public class LibraryStorageServiceImpl implements LibraryStorageService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public LibraryStorageServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }


    @Override
    @Transactional
    public GenericDaoResult<Book> addNewBook(String name, long authorId, String genreName) {
        final Optional<Author> author = authorRepository.getById(authorId);
        if (!author.isPresent()) {
            return GenericDaoResult.createError("Author not found");
        } else {
            return bookRepository.insert(name, author.get(), genreName);
        }
    }

    @Override
    @Transactional
    public boolean deleteBook(long bookId) {
        return bookRepository.deleteById(bookId);
    }
}
