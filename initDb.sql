create database hsbc;

create table trade (
  id bigint(20) not null auto_increment,
  trade_date bigint(20) not null,
  trade_amount int not null,
  trade_type varchar(20) not null,
  currency_pair varchar(20) not null,
  primary key (id)
);
