drop table if exists users;
create table if not exists users ( id serial primary key,
                                   email  varchar,
                                   password varchar);