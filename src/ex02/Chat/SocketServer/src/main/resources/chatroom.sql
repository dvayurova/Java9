drop table if exists chatroom;
create table if not exists chatroom (id serial primary key, name varchar, ownerid int);