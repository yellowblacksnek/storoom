
create type location_types as enum ('stand', 'warehouse', 'main_office');
create type user_types as enum ('admin', 'superuser', 'client');
create type unit_types as enum ('S', 'M', 'L', 'XL');

create cast (varchar as user_types) with inout as implicit;
create cast (varchar as unit_types) with inout as implicit;
create cast (varchar as location_types) with inout as implicit;

create table if not exists Manufacturers (
    id uuid,
    name varchar(255) not null,

    primary key(id)
);

create table if not exists Locks (
      id uuid,
      manufacturer_id uuid not null,

      primary key(id),
      foreign key(manufacturer_id) references Manufacturers(id) on delete set null
);

create table if not exists Companies (
      id uuid,
      company_name varchar(255) not null,

      primary key(id)
);

create table if not exists Owners (
     id uuid,
     owner_name varchar(255) not null,
     company_id uuid not null,
     primary key(id),
     foreign key (company_id) references Companies(id)
);

create table if not exists Locations (
    id uuid,
    address varchar(255),
    location_type location_types,

    primary key (id)
);

create table if not exists Owners_Locations (
    owner_id uuid,
    location_id uuid,
    primary key(owner_id, location_id),
    foreign key (owner_id) references Owners(id),
    foreign key (location_id) references Locations(id)
);

create table if not exists Units (
    id uuid,
    unit_type unit_types not null,
    location_id uuid not null,
    lock_id uuid not null,
    is_available boolean,
    size_x int not null,
    size_y int not null,
    size_z int not null,

    primary key (id),
    foreign key (location_id) references Locations(id),
    foreign key (lock_id) references Locks(id)
    );

create table if not exists Users (
    id uuid,
    username varchar(255) not null unique,
    password varchar(255) not null,
    user_type user_types not null,

    primary key (id)
);

create table if not exists Orders (
    id uuid,
    unit_id uuid not null,
    user_id uuid not null,
    order_number int not null,
    created_time timestamp not null,
    days int not null,

    primary key (id),
    foreign key (unit_id) references Units(id),
    foreign key (user_id) references Users(id)
);