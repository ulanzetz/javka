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
    description varchar(100)
);

create unique index files_id_uindex
    on files (id);

create unique index files_name_uindex
    on files (name);

create unique index files_name_uindex_2
    on files (name);

alter table files
    add constraint files_pk
        unique (name);

alter table files
    add constraint files_users_id_fk
        foreign key (creator) references users;

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