CREATE TABLE web_post
(
    id              SERIAL PRIMARY KEY,
    from_id         INT REFERENCES web_user (id)    NOT NULL,
    to_id           INT REFERENCES web_user (id)    NOT NULL,
    title           VARCHAR                         NOT NULL,
    description     VARCHAR,
    created         TIMESTAMP                       NOT NULL,
    is_active       BOOLEAN                         NOT NULL
);