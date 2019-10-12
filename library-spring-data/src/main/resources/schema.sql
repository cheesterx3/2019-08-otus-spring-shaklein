create table author
(
    id   bigint auto_increment,
    name varchar(255),
    primary key (id)
);

create table genre
(
    id   bigint auto_increment,
    name varchar(255),
    primary key (id)
);

create table book
(
    id   bigint auto_increment,
    name varchar(255),
    primary key (id)
);

create table comment
(
    id      bigint auto_increment,
    text    varchar(1000),
    dtime   timestamp,
    book_id bigint references book (id) on delete cascade,
    primary key (id)
);

create table ref_book_author
(
    book_id   bigint references book (id) on delete cascade,
    author_id bigint references author (id) on delete cascade,
    primary key (book_id, author_id)
);

create table ref_book_genre
(
    book_id  bigint references book (id) on delete cascade,
    genre_id bigint references genre (id) on delete cascade,
    primary key (book_id, genre_id)
);
