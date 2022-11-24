drop table if exists owners_locations;
drop table if exists locations;
drop table if exists owners;
drop table if exists companies;
drop cast if exists (varchar as location_types);
drop type if exists location_types;

create type location_types as enum ('stand', 'warehouse', 'main_office');
create cast (varchar as location_types) with inout as implicit;

CREATE TABLE IF NOT EXISTS companies
(
    id           UUID         NOT NULL default gen_random_uuid(),
    company_name VARCHAR(255) NOT NULL,
    CONSTRAINT companies_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS owners
(
    id         UUID         NOT NULL default gen_random_uuid(),
    owner_name VARCHAR(255) NOT NULL,
    company_id UUID         NOT NULL,
    CONSTRAINT owners_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS locations
(
    id            UUID NOT NULL default gen_random_uuid(),
    address       VARCHAR(255),
    location_type LOCATION_TYPES,
    CONSTRAINT locations_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS owners_locations
(
    owner_id    UUID NOT NULL,
    location_id UUID NOT NULL,
    CONSTRAINT owners_locations_pkey PRIMARY KEY (owner_id, location_id)
);

insert into companies(id, company_name) values('527af30f-a4cc-43b5-a2fc-745722aee05c', 'company');
insert into locations(id, address, location_type) values('527af30f-a4cc-43b5-a2fc-745722aee05c', 'address', 'stand');
insert into owners(id, owner_name, company_id) values('527af30f-a4cc-43b5-a2fc-745722aee05c', 'owner', '527af30f-a4cc-43b5-a2fc-745722aee05c');
insert into owners_locations(owner_id, location_id) values('527af30f-a4cc-43b5-a2fc-745722aee05c', '527af30f-a4cc-43b5-a2fc-745722aee05c');


