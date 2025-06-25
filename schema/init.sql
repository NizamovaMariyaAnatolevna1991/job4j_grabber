create table post
(
    id              serial primary key,
    title           varchar(50),
    description     varchar(250),
    link            varchar(50) unique not null,
    time            date
);

commit;