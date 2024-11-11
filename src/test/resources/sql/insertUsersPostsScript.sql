SET REFERENTIAL_INTEGRITY TO FALSE;
TRUNCATE TABLE web_post RESTART IDENTITY;
TRUNCATE TABLE web_user RESTART IDENTITY;
SET REFERENTIAL_INTEGRITY TO TRUE;

INSERT INTO web_user (username, email, password, timezone)
VALUES ('All', 'All@mail.com', 'sdfhsdthwrdfv', null),
       ('John', 'John@gmail.com', 'password1', null),
       ('Kate', 'Kate@gmail.com', 'password2', null),
       ('Steve', 'Steve@mail.com', 'password3', null);

INSERT INTO web_post (from_id, to_id, title, created, is_active)
VALUES (2, 1, 'Hello All!', now(), true),
       (2, 3, 'Hi', now(), true),
       (2, 3, '???', now(), true),
       (2, 4, 'Hi', now(), true),
       (3, 2, 'Im busy', now(), true),
       (4, 2, 'take is easy', now(), true),
       (4, 3, 'Id like delete this message', now(), false);