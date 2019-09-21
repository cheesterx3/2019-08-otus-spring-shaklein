package ru.otus.study.spring.librarydao;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.study.spring.librarydao.repository.BookRepository;

import java.sql.SQLException;

@SpringBootApplication
public class LibraryDaoApplication {

    public static void main(String[] args) throws SQLException {
        final ApplicationContext applicationContext = SpringApplication.run(LibraryDaoApplication.class, args);
        BookRepository bookRepository=applicationContext.getBean(BookRepository.class);


    }

}
