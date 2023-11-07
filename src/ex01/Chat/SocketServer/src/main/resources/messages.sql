drop table if exists messages;
create table if not exists messages (id serial primary key, sender varchar, text varchar, time timestamp);