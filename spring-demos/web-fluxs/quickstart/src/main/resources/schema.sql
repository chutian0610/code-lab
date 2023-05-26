DROP TABLE IF EXISTS customer;
create table customer
(
    id bigint auto_increment,
    first_name varchar(255) default '' not null,
    last_name varchar(255) default '' not null,
    constraint customer_pk
        primary key (id)
);

insert into customer(id,first_name,last_name) values(1,"Chloe","O'Brian");
insert into customer(id,first_name,last_name) values(3,"Kim", "Bauer");
insert into customer(id,first_name,last_name) values(5,"David", "Palmer");
insert into customer(id,first_name,last_name) values(7,"Michelle", "Dessler");
