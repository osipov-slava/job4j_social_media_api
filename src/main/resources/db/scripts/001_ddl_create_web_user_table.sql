create table web_user
(
    id          serial  primary key,
    email       varchar not null unique,
    password    varchar not null,
    timezone    varchar
);

insert into web_user (email, password, timezone) values ('All', '', null);