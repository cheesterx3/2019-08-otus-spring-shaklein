package ru.otus.study.spring.librarymvc.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.otus.study.spring.librarymvc.domain.*;
import ru.otus.study.spring.librarymvc.securityacl.domain.DomainObjectPermission;
import ru.otus.study.spring.librarymvc.securityacl.domain.MongoAcl;
import ru.otus.study.spring.librarymvc.securityacl.domain.MongoSid;

import java.util.*;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {
    private static final String ROLE_LIBRARIAN = "ROLE_LIBRARIAN";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private List<Genre> genres = new ArrayList<>();
    private List<Author> authors = new ArrayList<>();
    private List<Book> books = new ArrayList<>();
    private List<Role> roles = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private Role librarianRole;
    private Role userRole;
    private Role adminRole;


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
        genres.add(template.save(new Genre("Документация")));
    }

    @ChangeSet(order = "002", id = "initAuthors", author = "alex", runAlways = true)
    public void initAuthors(MongoTemplate template) {
        authors.add(template.save(new Author("Толстой Л.Н.")));
        authors.add(template.save(new Author("Чехов А.П.")));
        authors.add(template.save(new Author("Горький Максим")));
        authors.add(template.save(new Author("Администратор системы")));
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
        books.add(template.save(new Book("Внуитренняя документация библиотеки", authors.get(3), genres.get(4))));
    }

    @ChangeSet(order = "007", id = "initComments", author = "alex", runAlways = true)
    public void initComments(MongoTemplate template) {
        template.save(new BookComment("Какая замечательная книга", books.get(0), users.get(1)));
        template.save(new BookComment("Какая нудная книга", books.get(0), users.get(2)));
        template.save(new BookComment("Очень поучительная книга", books.get(1), users.get(2)));
        template.save(new BookComment("Осилил", books.get(2), users.get(2)));
        template.save(new BookComment("А я нет", books.get(2), users.get(0)));
    }

    @ChangeSet(order = "005", id = "initRoles", author = "alex", runAlways = true)
    public void initRoles(MongoTemplate template) {
        adminRole = template.save(new Role(ROLE_ADMIN));
        userRole = template.save(new Role(ROLE_USER));
        librarianRole = template.save(new Role(ROLE_LIBRARIAN));
    }

    @ChangeSet(order = "006", id = "initUsers", author = "alex", runAlways = true)
    public void initUsers(MongoTemplate template) {
        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        users.add(template.save(new User("Администратор библиотеки", "admin", passwordEncoder.encode("password"), Collections.singletonList(adminRole))));
        users.add(template.save(new User("Александр Сергеевич", "user", passwordEncoder.encode("password"), Collections.singletonList(userRole))));
        users.add(template.save(new User("Иван Васильевич", "user1", passwordEncoder.encode("password"), Collections.singletonList(userRole))));
        users.add(template.save(new User("Библиотекарь", "librarian", passwordEncoder.encode("password"), Collections.singletonList(librarianRole))));
    }

    @ChangeSet(order = "007", id = "initPermissions", author = "alex", runAlways = true)
    public void initPermissions(MongoTemplate template) throws ClassNotFoundException {
        final MongoSid adminSid = template.save(new MongoSid("admin", true));
        final MongoSid librarianRoleSid = template.save(new MongoSid(ROLE_LIBRARIAN, false));
        final MongoSid adminRoleSid = template.save(new MongoSid(ROLE_ADMIN, false));
        final MongoSid anonymousRoleSid = template.save(new MongoSid("ROLE_ANONYMOUS", false));
        final MongoSid userGroupSid = template.save(new MongoSid(ROLE_USER, false));

        final Book bookToProtect = books.get(3);

        final ObjectIdentity oid = new ObjectIdentityImpl(Class.forName(bookToProtect.getClass().getName()), bookToProtect.getId());
        final ObjectIdentity oid1 = new ObjectIdentityImpl(Class.forName(bookToProtect.getClass().getName()), books.get(0).getId());
        final ObjectIdentity oid2 = new ObjectIdentityImpl(Class.forName(bookToProtect.getClass().getName()), books.get(1).getId());
        final ObjectIdentity oid3 = new ObjectIdentityImpl(Class.forName(bookToProtect.getClass().getName()), books.get(2).getId());

        final MongoAcl mongoAcl = new MongoAcl(oid.getIdentifier(), oid.getType(), adminSid, null, false);

        /*
         * Пришлось разделить каждое право на отдельный объект, т.к. при тесте spring использует DefaultPermissionGrantingStrategy,
         * который не учитывает вхождение права в маску
         */
        //<editor-fold desc="Админские права">
        final DomainObjectPermission adminGroupReadPermission = createPermissionObject(adminRoleSid, BasePermission.READ.getMask(), true);
        final DomainObjectPermission adminGroupAdminPermission = createPermissionObject(adminRoleSid, BasePermission.ADMINISTRATION.getMask(), true);
        final DomainObjectPermission librarianReadOnlyPermission = createPermissionObject(librarianRoleSid, BasePermission.READ.getMask(), true);
        final DomainObjectPermission librarianAdminPermission = createPermissionObject(librarianRoleSid, BasePermission.ADMINISTRATION.getMask(), true);
        //</editor-fold>

        //<editor-fold desc="Пользовательские и анонимные права">
        final DomainObjectPermission usersObjectPermission = createPermissionObject(userGroupSid, BasePermission.READ.getMask(), true);
        final DomainObjectPermission noUsersObjectPermission = createPermissionObject(userGroupSid, 0, false);
        final DomainObjectPermission noAnonymousPermission = createPermissionObject(anonymousRoleSid, 0, false);
        final DomainObjectPermission anonymousObjectPermission = createPermissionObject(anonymousRoleSid, BasePermission.READ.getMask(), true);
        //</editor-fold>

        mongoAcl.setPermissions(Arrays.asList(adminGroupReadPermission, adminGroupAdminPermission, librarianReadOnlyPermission, noUsersObjectPermission, noAnonymousPermission));
        template.save(mongoAcl);

        final List<DomainObjectPermission> permissions = Arrays.asList(adminGroupReadPermission, adminGroupAdminPermission,
                librarianAdminPermission, librarianReadOnlyPermission,
                usersObjectPermission, anonymousObjectPermission);

        template.save(createAcl(adminSid, oid1, permissions));
        template.save(createAcl(adminSid, oid2, permissions));
        template.save(createAcl(adminSid, oid3, permissions));
    }

    private MongoAcl createAcl(MongoSid adminSid, ObjectIdentity oid, List<DomainObjectPermission> permissions) {
        final MongoAcl mongoAcl = new MongoAcl(oid.getIdentifier(), oid.getType(), adminSid, null, false);
        mongoAcl.setPermissions(permissions);
        return mongoAcl;
    }

    private DomainObjectPermission createPermissionObject(MongoSid userGroupSid, int mask, boolean b) {
        return new DomainObjectPermission(UUID.randomUUID().toString(), userGroupSid,
                mask, b, false, false);
    }
}
