package ru.otus.study.spring.librarymvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.otus.study.spring.librarymvc.domain.*;
import ru.otus.study.spring.librarymvc.exception.DaoException;
import ru.otus.study.spring.librarymvc.exception.NotFoundException;
import ru.otus.study.spring.librarymvc.security.CustomUserPrincipal;
import ru.otus.study.spring.librarymvc.service.LibraryReaderService;
import ru.otus.study.spring.librarymvc.service.LibraryStorageService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final LibraryReaderService readerService;
    private final LibraryStorageService storageService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/")
    public String bookList(Model model) {
        final List<Book> booksSortedByName = readerService.getAllBooksSortedByName();
        model.addAttribute("books", booksSortedByName);
        return "books";
    }

    @GetMapping("/book/{id}")
    public String bookView(@PathVariable(name = "id") String bookID, @ModelAttribute("commentForm") BookCommentForm form, Model model) throws NotFoundException {
        final Optional<Book> optionalBook = readerService.findBookById(bookID);
        return optionalBook.map(book -> {
            populateBookViewModel(form, model, book);
            return "book";
        }).orElseThrow(() -> new NotFoundException("Book not found"));
    }

    private void populateBookViewModel(@ModelAttribute("commentForm") BookCommentForm form, Model model, Book book) {
        final List<BookComment> bookComments = readerService.getBookComments(book.getId());
        model.addAttribute("book", book);
        model.addAttribute("comments", bookComments);
        form.setId(book.getId());
    }

    @PostMapping("/book/comment")
    public String commentBook(@ModelAttribute("commentForm") @Valid BookCommentForm form, BindingResult bindingResult, Model model) throws NotFoundException {
        if (bindingResult.hasErrors()) {
            final Optional<Book> bookById = readerService.findBookById(form.getId());
            bookById.ifPresent(book -> populateBookViewModel(form, model, book));
            return "book";
        }
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        readerService.commentBook(form.getId(), form.getComment(), userPrincipal.getUser());
        return String.format("redirect:/book/%s", form.getId());
    }

    @PostMapping("/book/{id}/delete")
    public String bookList(@PathVariable(name = "id") String bookID) {
        storageService.deleteBook(bookID);
        return "redirect:/";
    }

    @GetMapping("/book/add")
    public String addBookPage(@ModelAttribute(name = "bookForm") BookNewForm bookForm, Model model) {
        addDictionariesToModels(model);
        return "addBook";
    }

    @PostMapping("/book/add")
    public String addBook(@ModelAttribute(name = "bookForm") @Valid BookNewForm form, BindingResult bindingResult, Model model) throws DaoException {
        if (bindingResult.hasErrors()) {
            addDictionariesToModels(model);
            return "addBook";
        }
        storageService.addNewBookWithGenreId(form.getName(), form.getAuthorId(), form.getGenreId());
        return "redirect:/book/add";
    }

    @GetMapping("/book/{id}/update")
    public String editBookPage(@PathVariable("id") String bookId, @ModelAttribute("bookForm") BookEditForm form, Model model) throws NotFoundException {
        return readerService.findBookById(bookId)
                .map(book -> {
                    populateModelWithBook(form, book, model);
                    return "editBook";
                })
                .orElseThrow(() -> new NotFoundException("Book not found"));
    }

    @PostMapping("/book/update")
    public String editBook(@ModelAttribute("bookForm") @Valid BookEditForm form, BindingResult bindingResult, Model model) {
        final Optional<Book> bookById = readerService.findBookById(form.getId());
        if (bindingResult.hasErrors()) {
            bookById.ifPresent(book -> populateModelWithBook(form, book, model));
            return "editBook";
        }
        bookById.ifPresent(book -> {
            book.setName(form.getName());
            storageService.updateBook(book);
        });
        return String.format("redirect:/book/%s/update", form.getId());
    }

    @PostMapping("/book/{id}/genre/{genreId}/delete")
    public String deleteGenreFromBook(@PathVariable("id") String bookId, @PathVariable("genreId") String genreId) throws DaoException {
        storageService.removeGenreFromBook(bookId, genreId);
        return String.format("redirect:/book/%s/update", bookId);
    }

    @PostMapping("/book/{id}/author/{authorId}/delete")
    public String deleteAuthorFromBook(@PathVariable("id") String bookId, @PathVariable("authorId") String authorId) throws DaoException {
        storageService.removeAuthorFromBook(bookId, authorId);
        return String.format("redirect:/book/%s/update", bookId);
    }

    @PostMapping("/book/{id}/genre")
    public String addGenreToBook(@PathVariable("id") String bookId, @RequestParam("genreId") String genreId) throws DaoException {
        storageService.addGenreWithIdToBook(bookId, genreId);
        return String.format("redirect:/book/%s/update", bookId);
    }

    @PostMapping("/book/{id}/author")
    public String addAuthorToBook(@PathVariable("id") String bookId, @RequestParam("authorId") String authorId) throws DaoException {
        storageService.addAuthorToBook(bookId, authorId);
        return String.format("redirect:/book/%s/update", bookId);
    }

    private void populateModelWithBook(BookEditForm form, Book book, Model model) {
        model.addAttribute("book", book);
        model.addAttribute("filteredGenres", readerService.findAllGenres().stream().filter(genre -> !book.getGenres().contains(genre)).collect(Collectors.toList()));
        model.addAttribute("filteredAuthors", readerService.findAllAuthors().stream().filter(author -> !book.getAuthors().contains(author)).collect(Collectors.toList()));
        form.setId(book.getId());
        form.setName(book.getName());
    }

    private void addDictionariesToModels(Model model) {
        model.addAttribute("genres", readerService.findAllGenres());
        model.addAttribute("authors", readerService.findAllAuthors());
    }

}
