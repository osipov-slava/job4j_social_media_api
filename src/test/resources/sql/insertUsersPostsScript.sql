SET REFERENTIAL_INTEGRITY TO FALSE;
truncate table web_post restart identity;
truncate table web_user restart identity;
SET REFERENTIAL_INTEGRITY TO TRUE;

insert into web_user (email, password, timezone)
values ('All@mail.com', 'sdfhsdthwrdfv', null),
       ('John@gmail.com', 'password1', null),
       ('Kate@gmail.com', 'password2', null),
       ('Steve@mail.com', 'password3', null);

insert into web_post (from_id, to_id, title, created, is_active)
values (2, 1, 'Hello All!', now(), true),
       (2, 3, 'Hi', now(), true),
       (2, 3, '???', now(), true),
       (2, 4, 'Hi', now(), true),
       (3, 2, 'Im busy', now(), true),
       (4, 2, 'take is easy', now(), true),
       (4, 3, 'Id like delete this message', now(), false);