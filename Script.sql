drop table if exists account;
drop table if exists client;

create table client(
	id INTEGER primary key auto_increment,
	name VARCHAR(255) not null,
	age INTEGER default 0
);

create table account(
	id INTEGER primary key auto_increment,
	account_type VARCHAR(255) not null,
	amount INTEGER default 0,
	client_id int not null,
	constraint `fk_client_id` foreign key (client_id) references client(id) on delete cascade
);

insert into client
(name, age)
values
('Kyle Bass', 35),
('Jack Smack', 34)
;

update client
set name = "Kyle Bass"
where id = 1;

select *
from client;

insert into account
(account_type, amount, client_id)
values
('checking', 1000, 1),
('checking', 2000, 1),
('checking', 3000, 1),
('checking', 400, 1)
;

select *
from account
order by client_id;

UPDATE project0.account SET amount = 600 WHERE id = 1 and  client_id = 1;

SELECT * FROM project0.account a WHERE a.client_id = 1 AND amount >= 200;

SELECT * FROM project0.account a WHERE a.client_id = 1 AND amount between 400 and 1000;
