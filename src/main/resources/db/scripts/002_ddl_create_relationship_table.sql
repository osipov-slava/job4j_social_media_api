CREATE TABLE relationship
(
    id          SERIAL PRIMARY KEY,
    user_id     INT NOT NULL REFERENCES web_user (id),
    partner_id  INT NOT NULL REFERENCES web_user (id),
    type_friend VARCHAR,
        UNIQUE (user_id, partner_id)
);