CREATE TABLE friends
(
    id          serial PRIMARY KEY,
    user_id     int not null REFERENCES web_user (id),
    partner_id  int not null REFERENCES web_user (id),
    type_friend varchar,
                UNIQUE (user_id, partner_id)
);