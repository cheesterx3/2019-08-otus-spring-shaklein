insert into author(`name`)
values ('Толстой Л.Н.');
insert into author(`name`)
values ('Чехов А.П.');
insert into author(`name`)
values ('Горький Максим');

insert into genre(`name`)
values ('Роман');
insert into genre(`name`)
values ('Классика');
insert into genre(`name`)
values ('Детектив');
insert into genre(`name`)
values ('Сборник');

insert into book(id, `name`)
values (1, 'Война и мир');
insert into book(id, `name`)
values (2, 'На дне');
insert into book(id, `name`)
values (3, 'Анна Каренина');
insert into book(id, `name`)
values (4, 'Сборник рассказов и повестей');

insert into role(id, role)
values (1, 'ROLE_USER');
insert into role(id, role)
values (2, 'ROLE_ADMIN');
insert into role(id, role)
values (3, 'ROLE_LIBRARIAN');

insert into user(id, name, login, password)
values (1, 'User', 'user', 'password');
insert into user(id, name, login, password)
values (2, 'Admin', 'admin', 'password');
insert into user(id, name, login, password)
values (3, 'Librarian', 'librarian', 'password');

insert into ref_book_author(book_id, author_id)
values (1, 1);
insert into ref_book_author(book_id, author_id)
values (3, 1);
insert into ref_book_author(book_id, author_id)
values (2, 3);
insert into ref_book_author(book_id, author_id)
values (4, 2);
insert into ref_book_author(book_id, author_id)
values (4, 1);

insert into ref_book_genre(book_id, genre_id)
values (1, 1);
insert into ref_book_genre(book_id, genre_id)
values (1, 2);
insert into ref_book_genre(book_id, genre_id)
values (2, 2);
insert into ref_book_genre(book_id, genre_id)
values (3, 1);
insert into ref_book_genre(book_id, genre_id)
values (3, 2);
insert into ref_book_genre(book_id, genre_id)
values (4, 3);
insert into ref_book_genre(book_id, genre_id)
values (4, 2);

insert into comment(id, text, dtime, book_id, user_id)
values (1, 'Test comment', {ts '2019-09-30 10:00:00'}, 1, 1);
insert into comment(id, text, dtime, book_id, user_id)
values (2, 'Test comment 2', {ts '2019-09-30 10:01:00'}, 1, 3);
insert into comment(id, text, dtime, book_id, user_id)
values (3, 'Test comment 3', {ts '2019-09-30 10:02:00'}, 1, 1);



insert into ref_role_user(user_id, role_id)
values (1, 1);
insert into ref_role_user(user_id, role_id)
values (2, 1);
insert into ref_role_user(user_id, role_id)
values (2, 2);
insert into ref_role_user(user_id, role_id)
values (2, 3);
insert into ref_role_user(user_id, role_id)
values (3, 1);
insert into ref_role_user(user_id, role_id)
values (3, 3);





