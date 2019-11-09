create table users
(
    id   serial  not null
        constraint users_pk
            primary key,
    name varchar(100) not null,
    password_hash varchar(100) not null
);

create unique index users_id_uindex
    on users (id);

create table files
(
    id serial not null,
    name varchar(100) not null,
    creator int not null,
    parentId int,
    description varchar(100),
    path varchar(256) not null
);

create unique index files_id_uindex
    on files (id);

alter table files
    add constraint files_users_id_fk
        foreign key (creator) references users;
    add constraint files_files_id_fk
        foreign key (parent) references files;

create table groups
(
    id   serial  not null
        constraint groups_pk
            primary key,
    name varchar(100) not null,
    creator int not null
        constraint groups_users_id_fk
            references users (id)
);

create unique index groups_id_uindex
    on groups (id);

create table user_files
(
    user_id int not null
        constraint user_files_users_id_fk
            references users,
    file_id int not null
        constraint user_files_files_id_fk
            references files (id),
    constraint user_files_pk
        unique (user_id, file_id)
);

create table user_groups
(
    user_id int not null
        constraint user_groups_users_id_fk
            references users,
    group_id int not null
        constraint user_groups_groups_id_fk
            references groups (id),
    constraint user_groups_pk
        unique (user_id, group_id)
);

create table group_files
(
    group_id int not null
        constraint group_files_groups_id_fk
            references groups,
    file_id int not null
        constraint group_files_files_id_fk
            references files (id),
    constraint group_files_pk
        unique (group_id, file_id)
);