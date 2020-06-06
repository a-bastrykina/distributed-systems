create extension hstore;

drop table ways_tags;
drop table nodes_tags;
drop table relations_tags;

alter table node add column tags hstore;
alter table way add column tags hstore;
alter table relation add column tags hstore;