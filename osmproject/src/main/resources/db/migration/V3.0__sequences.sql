create sequence node_id_seq;
alter table node alter column id set default nextval('node_id_seq');
alter table node alter column id set not null;
alter sequence node_id_seq owned by node.id;

create sequence way_id_seq;
alter table way alter column id set default nextval('way_id_seq');
alter table way alter column id set not null;
alter sequence way_id_seq owned by way.id;

create sequence rel_id_seq;
alter table relation alter column id set default nextval('rel_id_seq');
alter table relation alter column id set not null;
alter sequence rel_id_seq owned by relation.id;

create sequence tag_id_seq;
alter table tag alter column id set default nextval('tag_id_seq');
alter table tag alter column id set not null;
alter sequence tag_id_seq owned by tag.id;

create sequence mem_id_seq;
alter table member alter column id set default nextval('mem_id_seq');
alter table member alter column id set not null;
alter sequence mem_id_seq owned by member.id;