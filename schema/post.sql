create table post
(
    id       serial primary key,
    name     varchar(50),
    text     varchar(250),
    link     varchar(50) unique not null,
    created   date
);

commit;