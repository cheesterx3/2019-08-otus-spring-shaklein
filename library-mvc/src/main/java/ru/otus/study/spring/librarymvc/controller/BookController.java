package ru.otus.study.spring.librarymvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.study.spring.librarymvc.domain.Book;
import ru.otus.study.spring.librarymvc.domain.BookEditForm;
import ru.otus.study.spring.librarymvc.domain.BookNewForm;
import ru.otus.study.spring.librarymvc.exception.DaoException;
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

    @ExceptionHandler(DaoException.class)
    public ModelAndView handleDaoException(DaoException e) {
        ModelAndView modelAndView = new ModelAndView("err500");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

    @GetMapping("/")
    public String bookList(Model model) {
        final List<Book> booksSortedByName = readerService.getAllBooksSortedByName();
        model.addAttribute("books", booksSortedByName);
        return "books";
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
    public String editBookPage(@PathVariable("id") String bookId, @ModelAttribute("bookForm") BookEditForm form, Model model) throws DaoException {
        return readerService.findBookById(bookId)
                .map(book -> {
                    populateModelWithBook(form, book, model);
                    return "editBook";
                })
                .orElseThrow(() -> new DaoException("Book not found"));
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
