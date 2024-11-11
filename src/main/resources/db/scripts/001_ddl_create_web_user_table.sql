CREATE TABLE web_user
(
    id          SERIAL  PRIMARY KEY,
    username    VARCHAR(20) NOT NULL UNIQUE,
    email       VARCHAR     NOT NULL UNIQUE,
    password    VARCHAR     NOT NULL,
    timezone    VARCHAR
);

INSERT INTO web_user (username, email, password, timezone)
VALUES ('All', 'All@mail.ru', 'sd86U^$RUYF', null);