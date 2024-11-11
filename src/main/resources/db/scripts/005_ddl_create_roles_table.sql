create table roles (
    id serial primary key,
    name varchar(20) not null unique
);