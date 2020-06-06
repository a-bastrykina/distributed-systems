alter table member add column relation_id bigint;

alter table member add constraint rel_id_fk foreign key (relation_id) references relation (id) on delete cascade on
update cascade;

drop table relations_members;