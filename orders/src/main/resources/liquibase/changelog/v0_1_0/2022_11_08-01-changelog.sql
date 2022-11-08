-- liquibase formatted sql

-- changeset sus:1665582553174-0
create type unit_types as enum ('S', 'M', 'L', 'XL');
create cast (varchar as unit_types) with inout as implicit;
create type unit_status as enum ('dead', 'available', 'occupied', 'pending');
create cast (varchar as unit_status) with inout as implicit;
create type order_status as enum ('active', 'finished');
create cast (varchar as order_status) with inout as implicit;

-- changeset sus:1665582553174-1
CREATE TABLE units
(
    id           UUID        NOT NULL,
    unit_type    unit_types  NOT NULL,
    location_id  UUID        NOT NULL,
    lock_id      UUID        NOT NULL,
    status       unit_status NOT NULL,
    size_x       INTEGER     NOT NULL,
    size_y       INTEGER     NOT NULL,
    size_z       INTEGER     NOT NULL,
    CONSTRAINT units_pkey PRIMARY KEY (id)
);

-- changeset sus:1665582553174-2
CREATE TABLE orders
(
    id            UUID    NOT NULL,
    unit_id       UUID    NOT NULL,
    user_id       UUID    NOT NULL,
    start_time  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    finished_time TIMESTAMP WITHOUT TIME ZONE,
    status        ORDER_STATUS NOT NULL,
    CONSTRAINT orders_pkey PRIMARY KEY (id)
);

-- changeset sus:1665582553174-3
ALTER TABLE orders
    ADD CONSTRAINT orders_unit_id_fkey FOREIGN KEY (unit_id) REFERENCES units (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

