CREATE TABLE my_db.users
(
    id              bigint NOT NULL AUTO_INCREMENT,
    username        varchar(80),
    password        varchar(80),
    email           varchar(80),
    active          tinyint(1),
    activation_code text,
    PRIMARY KEY (id)
);

CREATE TABLE my_db.roles
(
    id   int NOT NULL AUTO_INCREMENT,
    name varchar(15),
    PRIMARY KEY (id)
);

CREATE TABLE my_db.users_roles
(
    user_id bigint NOT NULL,
    role_id int    NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES my_db.users (id),
    FOREIGN KEY (role_id) REFERENCES my_db.roles (id)
);

insert into my_db.roles (name)
values ('ROLE_USER'),
       ('ROLE_ADMIN');

insert into my_db.users (username, password, email, active, activation_code)
values ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'user@gmail.com', 1, null);

insert into users_roles (user_id, role_id)
values (1, 1),
       (1, 2);