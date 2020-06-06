
alter table nodes_tags drop tag_id;
alter table ways_tags drop tag_id;
alter table relations_tags drop tag_id;

drop table tag;

alter table nodes_tags add column key varchar(255);
alter table nodes_tags add column val varchar(255);
alter table ways_tags add column key varchar(255);
alter table ways_tags add column val varchar(255);
alter table relations_tags add column key varchar(255);
alter table relations_tags add column val varchar(255);

