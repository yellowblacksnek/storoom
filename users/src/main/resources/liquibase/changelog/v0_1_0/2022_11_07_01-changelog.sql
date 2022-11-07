-- liquibase formatted sql

create type user_types as enum ('admin', 'superuser', 'client');
create cast (varchar as user_types) with inout as implicit;

-- changeset sus:1667850080697-1
CREATE TABLE users
(
    id        UUID         NOT NULL,
    username  VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    user_type VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

