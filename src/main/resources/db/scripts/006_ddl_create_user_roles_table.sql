create table user_roles (
    role_id int references roles(id),
    user_id int references web_user(id)
);