drop table if exists messages;
create table if not exists messages (id serial primary key, sender varchar, senderId int, text varchar, time timestamp default current_timestamp);