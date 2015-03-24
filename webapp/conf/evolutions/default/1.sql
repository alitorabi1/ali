# --- First database schema

# --- !Ups

set ignorecase true;

create table login (
  id                        bigint not null,
  password                      varchar(255) not null,
  constraint pk_company primary key (id))
;

# --- !Downs

drop table if exists login;