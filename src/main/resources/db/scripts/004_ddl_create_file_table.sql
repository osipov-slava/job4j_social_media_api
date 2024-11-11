CREATE TABLE file
(
    id      SERIAL  PRIMARY KEY,
    path    VARCHAR NOT NULL,
    name    VARCHAR NOT NULL ,
    post_id INT     REFERENCES web_post (id)
);
