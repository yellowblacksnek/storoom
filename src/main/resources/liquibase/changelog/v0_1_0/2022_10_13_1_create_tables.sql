-- liquibase formatted sql

-- changeset sus:1665582553174-0
create type location_types as enum ('stand', 'warehouse', 'main_office');
create type user_types as enum ('admin', 'superuser', 'client');
create type unit_types as enum ('S', 'M', 'L', 'XL');
create cast (varchar as user_types) with inout as implicit;
create cast (varchar as unit_types) with inout as implicit;
create cast (varchar as location_types) with inout as implicit;

-- changeset sus:1665582553174-1
CREATE TABLE users
(
    id        UUID         NOT NULL,
    username  VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    user_type USER_TYPES   NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

-- changeset sus:1665582553174-2
CREATE TABLE units
(
    id           UUID       NOT NULL,
    unit_type    UNIT_TYPES NOT NULL,
    location_id  UUID       NOT NULL,
    lock_id      UUID       NOT NULL,
    is_available BOOLEAN,
    size_x       INTEGER    NOT NULL,
    size_y       INTEGER    NOT NULL,
    size_z       INTEGER    NOT NULL,
    CONSTRAINT units_pkey PRIMARY KEY (id)
);

-- changeset sus:1665582553174-3
CREATE TABLE locations
(
    id            UUID NOT NULL,
    address       VARCHAR(255),
    location_type LOCATION_TYPES,
    CONSTRAINT locations_pkey PRIMARY KEY (id)
);

-- changeset sus:1665582553174-4
ALTER TABLE users
    ADD CONSTRAINT users_username_key UNIQUE (username);

-- changeset sus:1665582553174-5
CREATE TABLE companies
(
    id           UUID         NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    CONSTRAINT companies_pkey PRIMARY KEY (id)
);

-- changeset sus:1665582553174-6
CREATE TABLE locks
(
    id              UUID NOT NULL,
    manufacturer_id UUID NOT NULL,
    CONSTRAINT locks_pkey PRIMARY KEY (id)
);

-- changeset sus:1665582553174-7
CREATE TABLE manufacturers
(
    id   UUID         NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT manufacturers_pkey PRIMARY KEY (id)
);

-- changeset sus:1665582553174-8
CREATE TABLE orders
(
    id           UUID    NOT NULL,
    unit_id      UUID    NOT NULL,
    user_id      UUID    NOT NULL,
    order_number INTEGER NOT NULL,
    created_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    days         INTEGER NOT NULL,
    CONSTRAINT orders_pkey PRIMARY KEY (id)
);

-- changeset sus:1665582553174-9
CREATE TABLE owners
(
    id         UUID         NOT NULL,
    owner_name VARCHAR(255) NOT NULL,
    company_id UUID         NOT NULL,
    CONSTRAINT owners_pkey PRIMARY KEY (id)
);

-- changeset sus:1665582553174-10
CREATE TABLE owners_locations
(
    owner_id    UUID NOT NULL,
    location_id UUID NOT NULL,
    CONSTRAINT owners_locations_pkey PRIMARY KEY (owner_id, location_id)
);

-- changeset sus:1665582553174-11
ALTER TABLE locks
    ADD CONSTRAINT locks_manufacturer_id_fkey FOREIGN KEY (manufacturer_id) REFERENCES manufacturers (id) ON UPDATE NO ACTION ON DELETE SET NULL;

-- changeset sus:1665582553174-12
ALTER TABLE orders
    ADD CONSTRAINT orders_unit_id_fkey FOREIGN KEY (unit_id) REFERENCES units (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset sus:1665582553174-13
ALTER TABLE orders
    ADD CONSTRAINT orders_user_id_fkey FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset sus:1665582553174-14
ALTER TABLE owners
    ADD CONSTRAINT owners_company_id_fkey FOREIGN KEY (company_id) REFERENCES companies (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset sus:1665582553174-15
ALTER TABLE owners_locations
    ADD CONSTRAINT owners_locations_location_id_fkey FOREIGN KEY (location_id) REFERENCES locations (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset sus:1665582553174-16
ALTER TABLE owners_locations
    ADD CONSTRAINT owners_locations_owner_id_fkey FOREIGN KEY (owner_id) REFERENCES owners (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset sus:1665582553174-17
ALTER TABLE units
    ADD CONSTRAINT units_location_id_fkey FOREIGN KEY (location_id) REFERENCES locations (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset sus:1665582553174-18
ALTER TABLE units
    ADD CONSTRAINT units_lock_id_fkey FOREIGN KEY (lock_id) REFERENCES locks (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

