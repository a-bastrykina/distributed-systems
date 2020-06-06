alter table node
    rename user_name to username;

alter table node
    rename change_set to changeset;

alter table way
    rename user_name to username;

alter table way
    rename change_set to changeset;

alter table relation
    rename user_name to username;

alter table relation
    rename change_set to changeset;

alter table node_of_way
    rename order_ to ord;

drop table way_tag;
drop table relation_tag;
drop table relation_member;

create table tag
(
    id  bigint primary key,
    key varchar(255),
    val varchar(255)
);

create table relations_tags
(
    tag_id      bigint,
    relation_id bigint,

    foreign key (tag_id) references tag (id)
        on delete cascade
        on update cascade,
    foreign key (relation_id) references relation (id)
        on delete cascade
        on update cascade
);

create table ways_tags
(
    tag_id bigint,
    way_id bigint,

    foreign key (tag_id) references tag (id)
        on delete cascade
        on update cascade,
    foreign key (way_id) references way (id)
        on delete cascade
        on update cascade
);

create table nodes_tags
(
    tag_id  bigint,
    node_id bigint,

    foreign key (tag_id) references tag (id)
        on delete cascade
        on update cascade,
    foreign key (node_id) references node (id)
        on delete cascade
        on update cascade
);

create table member
(
    id   bigint primary key,
    type varchar(255),
    role varchar(255),
    ref  bigint
);

create table relations_members
(
    relation_id  bigint,
    member_id bigint,

    foreign key (relation_id) references relation (id)
        on delete cascade
        on update cascade,
    foreign key (member_id) references member (id)
        on delete cascade
        on update cascade
);