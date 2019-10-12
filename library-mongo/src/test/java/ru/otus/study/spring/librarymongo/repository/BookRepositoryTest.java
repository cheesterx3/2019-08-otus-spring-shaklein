package ru.otus.study.spring.librarymongo.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.study.spring.librarymongo.AbstractRepositoryTest;
import ru.otus.study.spring.librarymongo.domain.Book;
import ru.otus.study.spring.librarymongo.domain.BookComment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий работы с книгами должен ")
@ComponentScan("ru.otus.study.spring.librarymongo.events")
class BookRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookCommentRepository bookCommentRepository;

    @DisplayName("при удалении книги удалить все ссылающиеся на неё комментарии")
    @Test
    void shouldRemoveBookCommentsOnBookDeletion(){
        final List<Book> allBooks = bookRepository.findAll();
        final Book book = allBooks.get(0);

        bookRepository.delete(book);
        final List<BookComment> comments = bookCommentRepository.findAllByBook_Id(book.getId());
        assertThat(comments).isEmpty();
    }
}