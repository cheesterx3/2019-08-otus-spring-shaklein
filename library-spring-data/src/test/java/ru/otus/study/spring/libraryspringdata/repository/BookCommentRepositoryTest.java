package ru.otus.study.spring.libraryspringdata.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.study.spring.libraryspringdata.domain.Book;
import ru.otus.study.spring.libraryspringdata.domain.BookComment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий работы с комментариями для книг ")
@DataJpaTest
class BookCommentRepositoryTest {
    private final static int EXPECTED_ROW_COUNT = 3;

    @Autowired
    private TestEntityManager em;
    @Autowired
    private BookCommentRepository bookCommentRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void commentBook() {
        final String commentText = "Some comment";
        final Book book = em.find(Book.class, 1L);
        final BookComment bookComment = new BookComment(commentText, book);
        final BookComment expectedComment = bookCommentRepository.save(bookComment);
        assertThat(expectedComment).isNotNull()
                .matches(comment -> comment.getText().equals(commentText))
                .matches(comment -> comment.getId() > 0)
                .matches(comment -> comment.getBook() == book);

        final BookComment actualComment = em.find(BookComment.class, expectedComment.getId());
        assertThat(actualComment).isNotNull()
                .isEqualToComparingFieldByFieldRecursively(expectedComment);
    }

    @Test
    void getBookComments() {
        final Book book = em.find(Book.class, 1L);
        final List<BookComment> bookComments = bookCommentRepository.getAllByBook(book);
        assertThat(bookComments).isNotNull()
                .hasSize(EXPECTED_ROW_COUNT)
                .allMatch(comment -> !comment.getText().isEmpty())
                .allMatch(comment -> comment.getBook() == book);
    }
}