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







