create table web_post
(
    id              serial primary key,
    from_id         int references web_user (id)    not null,
    to_id           int references web_user (id)    not null,
    title           varchar                         not null,
    description     varchar,
    created         timestamp                       not null,
    is_active       boolean                         not null
);