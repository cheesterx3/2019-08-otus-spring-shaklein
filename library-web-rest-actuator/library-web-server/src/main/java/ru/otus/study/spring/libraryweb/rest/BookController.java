package ru.otus.study.spring.libraryweb.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.otus.study.spring.libraryweb.domain.Book;
import ru.otus.study.spring.libraryweb.exception.DaoException;
import ru.otus.study.spring.libraryweb.exception.NotFoundException;
import ru.otus.study.spring.libraryweb.rest.dto.BookDtoForSave;
import ru.otus.study.spring.libraryweb.rest.dto.BookDtoSimple;
import ru.otus.study.spring.libraryweb.service.BookPopularityService;
import ru.otus.study.spring.libraryweb.service.LibraryReaderService;
import ru.otus.study.spring.libraryweb.service.LibraryStorageService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final LibraryReaderService readerService;
    private final LibraryStorageService storageService;
    private final BookPopularityService bookPopularityService;

    @GetMapping("/api/books")
    public List<BookDtoSimple> bookList() {
        return readerService.getAllBooksSortedByName()
                .stream().map(BookDtoSimple::toDto).collect(toList());
    }

    @GetMapping("/api/books/{id}")
    public BookDtoSimple viewBook(@PathVariable(name = "id") String bookID) throws NotFoundException {
        return readerService.findBookById(bookID).map(book -> {
            bookPopularityService.viewBook(book);
            return BookDtoSimple.toDto(book);
        }).orElseThrow(() -> new NotFoundException(String.format("Book with id [%s] not found", bookID)));
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity deleteBook(@PathVariable(name = "id") String bookID) throws NotFoundException {
        if (storageService.deleteBook(bookID)) {
            return ResponseEntity.noContent().build();
        }
        throw new NotFoundException(String.format("Book with id [%s] not found", bookID));
    }

    @PostMapping("/api/books")
    public ResponseEntity<?> addBook(@RequestBody BookDtoForSave bookDtoForSave, UriComponentsBuilder uriComponentsBuilder) throws DaoException {
        final Optional<Book> book = storageService.addNewBook(
                bookDtoForSave.getName(),
                bookDtoForSave.getAuthorId(),
                bookDtoForSave.getGenreId());
        return book.map(bk ->
                ResponseEntity.created(getBookLocation(uriComponentsBuilder, bk.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BookDtoSimple.toDto(bk)))
                .orElseThrow(() -> new DaoException("Book create error"));
    }

    @PutMapping("/api/books/{id}")
    public ResponseEntity<?> saveBook(@PathVariable(name = "id") String bookID, @RequestBody BookDtoForSave bookDtoForSave) throws DaoException, NotFoundException {
        final Optional<Book> book = storageService.saveBook(bookID,
                bookDtoForSave.getName(),
                bookDtoForSave.getAuthorId(),
                bookDtoForSave.getGenreId());
        return book.map(bk ->
                ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(BookDtoSimple.toDto(bk)))
                .orElseThrow(() -> new DaoException("Book save error"));
    }

    private URI getBookLocation(UriComponentsBuilder uriComponentsBuilder, String bookId) {
        return uriComponentsBuilder.path("/api/books/{id}").buildAndExpand(bookId).toUri();
    }
}
