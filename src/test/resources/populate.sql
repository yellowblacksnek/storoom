insert into manufacturers(id,name) values(gen_random_uuid(), 'manu');

insert into locks (id, name, manufacturer_id)
values (gen_random_uuid(), 'lock', (select id from manufacturers limit 1));

insert into locations (id, address, location_type)
values (gen_random_uuid(), 'addr', 'stand');

insert into units (id, unit_type, location_id, lock_id, status, size_x, size_y, size_z)
values (gen_random_uuid(), 'S', (select id from locations limit 1), (select id from locks limit 1), 'available', 10, 10, 10);

insert into users (id, username, password, user_type)
values (gen_random_uuid(), 'name', '$2y$10$./GKCBLTxm9Rzl97h/ZdQ.OE57lQG.igkpVl1mKDz6/RhQe6TTiPa', 'client');
-- pass

insert into users (id, username, password, user_type)
values (gen_random_uuid(), 'name1', '$2y$10$./GKCBLTxm9Rzl97h/ZdQ.OE57lQG.igkpVl1mKDz6/RhQe6TTiPa', 'client');
-- pass

insert into users (id, username, password, user_type)
values (gen_random_uuid(), 'name2', '$2y$10$./GKCBLTxm9Rzl97h/ZdQ.OE57lQG.igkpVl1mKDz6/RhQe6TTiPa', 'superuser');
-- pass

