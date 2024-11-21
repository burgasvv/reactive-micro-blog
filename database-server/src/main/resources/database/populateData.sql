
create table if not exists authority(
    id serial primary key ,
    name varchar not null
);

create table if not exists identity(
    id serial primary key ,
    username varchar unique not null ,
    password varchar unique not null ,
    email varchar unique not null ,
    firstname varchar ,
    lastname varchar ,
    patronymic varchar ,
    enabled boolean not null ,
    authority_id serial references authority(id)
        on UPDATE cascade on DELETE set null
);

create table if not exists friendship(
    identity_id serial references identity(id) on UPDATE cascade on DELETE cascade ,
    friend_id serial references identity(id) on UPDATE cascade on DELETE cascade ,
    accepted boolean not null ,
    primary key (identity_id, friend_id)
);

create table if not exists post(
    id serial primary key ,
    identity_id serial references identity(id) on UPDATE cascade on DELETE cascade ,
    content text not null ,
    published_at timestamp not null
);

create table if not exists comment(
    id serial primary key ,
    post_id serial references post(id) on UPDATE cascade on DELETE cascade ,
    identity_id serial references identity(id) on UPDATE cascade on DELETE cascade ,
    content text not null ,
    published_at timestamp not null
);

create table if not exists chat(
    id serial primary key ,
    sender_id serial references identity(id) on UPDATE cascade on DELETE cascade ,
    receiver_id serial references identity(id) on UPDATE cascade on DELETE cascade
);

create table if not exists message(
    id serial primary key ,
    chat_id serial references chat(id) on UPDATE cascade on DELETE cascade ,
    sender_id serial references identity(id) on UPDATE cascade on DELETE cascade ,
    receiver_id serial references identity(id) on UPDATE cascade on DELETE cascade ,
    content text not null ,
    received_at timestamp not null
);

create table if not exists community(
    id serial primary key ,
    title varchar not null ,
    description text ,
    is_public boolean not null ,
    open_post boolean not null ,
    open_comment boolean not null ,
    created_at timestamp not null
);

create table if not exists identity_community(
    id serial primary key ,
    identity_id serial references identity(id) on UPDATE cascade on DELETE cascade ,
    community_id serial references community(id) on UPDATE cascade on DELETE cascade ,
    owner boolean not null
);

create table if not exists community_post(
    id serial primary key ,
    community_id serial references community(id) on UPDATE cascade on DELETE cascade ,
    post_id serial references post(id) on UPDATE cascade on DELETE cascade
);

insert into authority(name) values ('ADMIN');
insert into authority(name) values ('USER');

insert into identity(username, password, email, firstname, lastname, patronymic, enabled, authority_id)
VALUES ('admin','$2a$10$mOlLnkPNKLUCrNEq0fMrMOh1fPRx//sauI0E2ojZyZjsgjbE7ZlNC','burgassme@gmail.com',
        'Бургас','Вячеслав','Васильевич',true, 1);
insert into identity(username, password, email, firstname, lastname, patronymic, enabled, authority_id)
VALUES ('user','$2a$10$N6c0cWUGpm7G7xxUZ4lCLOXY.3fXkuC9F3wvruJjN4Mwhya8J8ALC','karpov@gmail.com',
        'Карпов','Алексей','Викторович',true, 2);