
create type location_types as enum ('');
create type user_types as enum ('admin', 'client');
create type unit_types as enum ('');

create cast (varchar as user_types) with inout as implicit;
create cast (varchar as unit_types) with inout as implicit;
create cast (varchar as location_types) with inout as implicit;

create table if not exists Manufacturers (
    id serial,
    name varchar(255) not null,

    primary key(id)
);

create table if not exists Locks (
      id serial,
      manufacturer_id int not null,

      primary key(id),
      foreign key(manufacturer_id) references Manufacturers(id) on delete set null
);

create table if not exists Companies (
      id serial,
      company_name varchar(255) not null,

      primary key(id)
);

create table if not exists Owners (
     id serial,
     owner_name varchar(255) not null,
     company_id int not null,
     primary key(id),
     foreign key (company_id) references Companies(id)
);

create table if not exists Locations (
    id serial,
    address varchar(255),
    location_type location_types,

    primary key (id)
);

create table if not exists Owners_Locations (
    owner_id int,
    location_id int,
    primary key(owner_id, location_id),
    foreign key (owner_id) references Owners(id),
    foreign key (location_id) references Locations(id)
);

create table if not exists Units (
    id serial,
    unit_type unit_types not null,
    location_id int not null,

    primary key (id),
    foreign key (location_id) references Locations(id)
);

create table if not exists Users (
    id serial,
    username varchar(255) not null,
    password varchar(255) not null,
    user_type user_types not null,

    primary key (id)
);

create table if not exists Orders (
    id serial,
    unit_id int not null,
    user_id int not null,
    order_number int not null,
    order_date timestamp not null,

    primary key (id),
    foreign key (unit_id) references Units(id),
    foreign key (user_id) references Users(id)
);