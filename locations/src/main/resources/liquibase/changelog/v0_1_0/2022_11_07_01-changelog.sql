-- liquibase formatted sql
-- changeset sus:1665582553174-0
create type location_types as enum ('stand', 'warehouse', 'main_office');
create cast (varchar as location_types) with inout as implicit;

-- changeset sus:1665582553174-1
CREATE TABLE companies
(
    id           UUID         NOT NULL default gen_random_uuid(),
    company_name VARCHAR(255) NOT NULL,
    CONSTRAINT companies_pkey PRIMARY KEY (id)
);

-- changeset sus:1665582553174-2
CREATE TABLE owners
(
    id         UUID         NOT NULL default gen_random_uuid(),
    owner_name VARCHAR(255) NOT NULL,
    company_id UUID         NOT NULL,
    CONSTRAINT owners_pkey PRIMARY KEY (id)
);

-- changeset sus:1665582553174-3
CREATE TABLE locations
(
    id            UUID NOT NULL default gen_random_uuid(),
    address       VARCHAR(255),
    location_type LOCATION_TYPES,
    CONSTRAINT locations_pkey PRIMARY KEY (id)
);

-- changeset sus:1665582553174-4
CREATE TABLE owners_locations
(
    owner_id    UUID NOT NULL,
    location_id UUID NOT NULL,
    CONSTRAINT owners_locations_pkey PRIMARY KEY (owner_id, location_id)
);

