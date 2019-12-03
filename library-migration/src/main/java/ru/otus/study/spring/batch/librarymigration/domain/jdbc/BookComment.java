package ru.otus.study.spring.batch.librarymigration.domain.jdbc;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "text")
    private String text;
    @Column(name = "dtime")
    private LocalDateTime time;
    @OneToOne(targetEntity = Book.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_id")
    private Book book;
    @OneToOne(targetEntity = User.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

}
