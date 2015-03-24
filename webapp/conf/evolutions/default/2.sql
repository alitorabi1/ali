# --- Sample dataset

# --- !Ups

insert into login (id,password) values (  1,'Apple Inc.');
insert into login (id,password) values (  2,'Thinking Machines');
insert into login (id,password) values (  3,'RCA');
insert into login (id,password) values (  4,'Netronics');
insert into login (id,password) values (  5,'Tandy Corporation');

# --- !Downs

delete from login;