
create table if not exists authority(
    id bigint generated by default as identity unique ,
    name varchar not null
);

create table if not exists identity(
    id bigint generated by default as identity unique ,
    username varchar unique not null ,
    password varchar unique not null ,
    email varchar unique not null ,
    firstname varchar ,
    lastname varchar ,
    patronymic varchar ,
    enabled boolean not null ,
    authority_id bigint references authority(id)
        on UPDATE cascade on DELETE set null
);

create table if not exists friendship(
    identity_id bigint references identity(id) on UPDATE cascade on DELETE cascade ,
    friend_id bigint references identity(id) on UPDATE cascade on DELETE cascade ,
    accepted boolean not null ,
    primary key (identity_id, friend_id)
);

create table if not exists community(
    id bigint generated by default as identity unique ,
    title varchar not null ,
    description text ,
    is_public boolean not null ,
    open_post boolean not null ,
    open_comment boolean not null ,
    created_at timestamp not null
);

create table if not exists identity_community(
     identity_id bigint references identity(id) on UPDATE cascade on DELETE cascade ,
     community_id bigint references community(id) on UPDATE cascade on DELETE cascade ,
     owner boolean not null ,
     primary key (identity_id, community_id)
);

create table if not exists community_invitation(
    identity_id bigint references identity(id) on UPDATE cascade on DELETE cascade ,
    receiver_id bigint references identity(id) on UPDATE cascade on DELETE cascade ,
    community_id bigint references community(id) on UPDATE cascade on DELETE cascade ,
    is_accepted boolean not null ,
    primary key (identity_id, community_id, receiver_id)
);

create table if not exists wall(
    id bigint generated by default as identity unique ,
    identity_id bigint unique references identity(id) on UPDATE cascade on DELETE cascade ,
    community_id  bigint unique references community(id) on UPDATE cascade on DELETE cascade ,
    is_opened boolean not null
);

create table if not exists post(
    id bigint generated by default as identity unique ,
    identity_id bigint references identity(id) on UPDATE cascade on DELETE cascade ,
    wall_id bigint references wall(id) on UPDATE cascade on DELETE cascade ,
    content text not null ,
    published_at timestamp not null
);

create table if not exists comment(
    id bigint generated by default as identity unique ,
    post_id bigint references post(id) on UPDATE cascade on DELETE cascade ,
    identity_id bigint references identity(id) on UPDATE cascade on DELETE cascade ,
    content text not null ,
    published_at timestamp not null
);

create table if not exists chat(
    id bigint generated by default as identity unique ,
    sender_id bigint references identity(id) on UPDATE cascade on DELETE cascade ,
    receiver_id bigint references identity(id) on UPDATE cascade on DELETE cascade
);

create table if not exists message(
    id bigint generated by default as identity unique ,
    chat_id bigint references chat(id) on UPDATE cascade on DELETE cascade ,
    sender_id bigint references identity(id) on UPDATE cascade on DELETE cascade ,
    receiver_id bigint references identity(id) on UPDATE cascade on DELETE cascade ,
    content text not null ,
    received_at timestamp not null
);

insert into authority(name) values ('ADMIN');
insert into authority(name) values ('USER');

insert into identity(username, password, email, firstname, lastname, patronymic, enabled, authority_id)
VALUES ('admin','$2a$10$mOlLnkPNKLUCrNEq0fMrMOh1fPRx//sauI0E2ojZyZjsgjbE7ZlNC','burgassme@gmail.com',
        'Бургас','Вячеслав','Васильевич',true, 1);
insert into identity(username, password, email, firstname, lastname, patronymic, enabled, authority_id)
VALUES ('user','$2a$10$N6c0cWUGpm7G7xxUZ4lCLOXY.3fXkuC9F3wvruJjN4Mwhya8J8ALC','karpov@gmail.com',
        'Карпов','Алексей','Викторович',true, 2);

insert into wall(identity_id, community_id, is_opened) VALUES (1, null, false);
insert into wall(identity_id, community_id, is_opened) VALUES (2, null, true);