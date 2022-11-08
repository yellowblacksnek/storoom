insert into manufacturers(id,name) values(gen_random_uuid(), 'manu');

insert into locks (id, name, manufacturer_id)
values (gen_random_uuid(), 'lock', (select id from manufacturers limit 1));
