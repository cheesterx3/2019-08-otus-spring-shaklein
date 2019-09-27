insert into author(`name`) values ('Author1');
insert into author(`name`) values ('Author2');
insert into author(`name`) values ('Author3');

insert into genre(`name`) values ('Genre1');
insert into genre(`name`) values ('Genre2');
insert into genre(`name`) values ('Genre3');
insert into genre(`name`) values ('Genre4');

insert into book(id,`name`) values (1,'Book1');
insert into book(id,`name`) values (2,'Book2');
insert into book(id,`name`) values (3,'Book3');
insert into book(id,`name`) values (4,'Book4');

insert into ref_book_author(book_id,author_id) values (1,1);
insert into ref_book_author(book_id,author_id) values (3,1);
insert into ref_book_author(book_id,author_id) values (2,3);
insert into ref_book_author(book_id,author_id) values (4,2);
insert into ref_book_author(book_id,author_id) values (4,1);

insert into ref_book_genre(book_id,genre_id) values (1,1);
insert into ref_book_genre(book_id,genre_id) values (1,2);
insert into ref_book_genre(book_id,genre_id) values (2,2);
insert into ref_book_genre(book_id,genre_id) values (3,1);
insert into ref_book_genre(book_id,genre_id) values (3,2);
insert into ref_book_genre(book_id,genre_id) values (4,3);
insert into ref_book_genre(book_id,genre_id) values (4,2);







