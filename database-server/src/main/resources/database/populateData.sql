
create table if not exists authority(
    id serial primary key ,
    name varchar
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

insert into authority(name) values ('ADMIN');
insert into authority(name) values ('USER');

insert into identity(username, password, email, firstname, lastname, patronymic, enabled, authority_id)
VALUES ('admin','$2a$10$mOlLnkPNKLUCrNEq0fMrMOh1fPRx//sauI0E2ojZyZjsgjbE7ZlNC','burgassme@gmail.com',
        'Бургас','Вячеслав','Васильевич',true, 1);
insert into identity(username, password, email, firstname, lastname, patronymic, enabled, authority_id)
VALUES ('user','$2a$10$N6c0cWUGpm7G7xxUZ4lCLOXY.3fXkuC9F3wvruJjN4Mwhya8J8ALC','karpov@gmail.com',
        'Карпов','Алексей','Викторович',true, 2);

insert into post(identity_id, content, published_at) values (
    1, 'На сегодняшний момент язык Java является одним из самых распространенных и популярных ' ||
       'языков программирования. Первая версия языка появилась еще в 1996 году в недрах компании Sun Microsystems, ' ||
       'впоследствии поглощенной компанией Oracle.', '2024-11-15 09:40:32'
);

insert into comment(post_id, identity_id, content, published_at) values (
    1, 2, 'Хорош, красава, молодец! Полезная информация, Админ!', '2024-11-15 11:26:14'
);