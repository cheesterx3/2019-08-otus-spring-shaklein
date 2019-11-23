package ru.otus.study.spring.librarymvc.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.otus.study.spring.librarymvc.domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {
    private List<Genre> genres = new ArrayList<>();
    private List<Author> authors = new ArrayList<>();
    private List<Book> books = new ArrayList<>();
    private List<Role> roles = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    @ChangeSet(order = "000", id = "dropDB", author = "alex", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initGenres", author = "alex", runAlways = true)
    public void initGenres(MongoTemplate template) {
        genres.add(template.save(new Genre("Классика")));
        genres.add(template.save(new Genre("Роман")));
        genres.add(template.save(new Genre("Детектив")));
        genres.add(template.save(new Genre("Сборник")));
    }

    @ChangeSet(order = "002", id = "initAuthors", author = "alex", runAlways = true)
    public void initAuthors(MongoTemplate template) {
        authors.add(template.save(new Author("Толстой Л.Н.")));
        authors.add(template.save(new Author("Чехов А.П.")));
        authors.add(template.save(new Author("Горький Максим")));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "alex", runAlways = true)
    public void initBooks(MongoTemplate template) {
        final Book warAnPeaceBook = new Book("Война и мир", authors.get(0), genres.get(0));
        warAnPeaceBook.getGenres().add(genres.get(1));
        books.add(template.save(warAnPeaceBook));
        final Book secondBook = new Book("На дне", authors.get(2), genres.get(1));
        books.add(template.save(secondBook));
        final Book thirdBook = new Book("Сборник рассказов и повестей", authors.get(1), genres.get(2));
        thirdBook.getGenres().add(genres.get(0));
        thirdBook.getAuthors().add(authors.get(2));
        books.add(template.save(thirdBook));
    }

    @ChangeSet(order = "007", id = "initComments", author = "alex", runAlways = true)
    public void initComments(MongoTemplate template) {
        template.save(new BookComment("Какая замечательная книга", books.get(0),users.get(1)));
        template.save(new BookComment("Какая нудная книга", books.get(0),users.get(2)));
        template.save(new BookComment("Очень поучительная книга", books.get(1),users.get(2)));
        template.save(new BookComment("Осилил", books.get(2),users.get(2)));
        template.save(new BookComment("А я нет", books.get(2),users.get(0)));
    }

    @ChangeSet(order = "005", id = "initRoles", author = "alex", runAlways = true)
    public void initRoles(MongoTemplate template) {
        roles.add(template.save(new Role("ROLE_ADMIN")));
        roles.add(template.save(new Role("ROLE_USER")));
    }

    @ChangeSet(order = "006", id = "initUsers", author = "alex", runAlways = true)
    public void initUsers(MongoTemplate template) {
        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        users.add(template.save(new User("Администратор библиотеки", "admin", passwordEncoder.encode("password"), roles)));
        users.add(template.save(new User("Александр Сергеевич", "user", passwordEncoder.encode("password"), Collections.singletonList(roles.get(1)))));
        users.add(template.save(new User("Иван Васильевич", "user1", passwordEncoder.encode("password"), Collections.singletonList(roles.get(1)))));
    }
}
