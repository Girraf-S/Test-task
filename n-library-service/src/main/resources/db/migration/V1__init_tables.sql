create table borrowed_books
(
    id            bigint,
    borrowed_date timestamp(0),
    return_date   timestamp(0),
    status        varchar(255) not null,
    primary key (id)
);