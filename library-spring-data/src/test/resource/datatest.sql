insert into author(`name`)
values ('Author1');
insert into author(`name`)
values ('Author2');
insert into author(`name`)
values ('Author3');

insert into genre(`name`)
values ('Genre1');
insert into genre(`name`)
values ('Genre2');
insert into genre(`name`)
values ('Genre3');
insert into genre(`name`)
values ('Genre4');

insert into book(id, `name`)
values (1, 'Book1');
insert into book(id, `name`)
values (2, 'Book2');
insert into book(id, `name`)
values (3, 'Book3');
insert into book(id, `name`)
values (4, 'Book4');

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

insert into comment(id, text, dtime, book_id)
values (1, 'Test comment', {ts '2019-09-30 10:00:00'}, 1);
insert into comment(id, text, dtime, book_id)
values (2, 'Test comment 2', {ts '2019-09-30 10:01:00'}, 1);
insert into comment(id, text, dtime, book_id)
values (3, 'Test comment 3', {ts '2019-09-30 10:02:00'}, 1);








