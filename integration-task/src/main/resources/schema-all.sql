create table objects
(
    id  bigint,
    name varchar(255),
    primary key (id)
);

create table sensors
(
    id   bigint auto_increment,
    code varchar(32),
    object_id bigint references objects (id) on delete cascade,
    primary key (id)
);

create table object_events
(
    id   bigint auto_increment,
    type varchar(255),
    message varchar(1024),
    object_id bigint references objects (id) on delete cascade,
    primary key (id)
);

create table accidents
(
    id   bigint auto_increment,
    type varchar(255),
    info varchar(1024),
    message_id bigint references object_events (id) on delete cascade,
    object_id bigint references objects (id) on delete cascade,
    primary key (id)
);
