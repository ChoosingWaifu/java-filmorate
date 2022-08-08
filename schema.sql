CREATE TABLE genres (
                        genre_id int PRIMARY KEY,
                        genre varchar
);

CREATE TABLE MPA
(
    MPA_id int2,
    MPA    varchar,
    constraint P_KEY
        primary key (MPA_id)
);

CREATE TABLE status (
                        status_id binary primary key ,
                        status varchar(15)
);

create table films (
                       id int8 PRIMARY KEY,
                       name CHARACTER VARYING(60) NOT NULL,
                       description varchar(200),
                       release_date timestamp,
                       duration int,
                       genre_id int,
                       MPA_id int,
                       constraint MPA_ID
                           foreign key (MPA_id) references MPA(MPA_id),
                       constraint GENRE_ID
                           foreign key (genre_id) references genres(genre_id)
);

CREATE TABLE users (
                         id int8 PRIMARY KEY,
                             email varchar NOT NULL,
                         login varchar NOT NULL,
                         name varchar,
                         birthday timestamp
);

create unique index USERS_LOGIN_UNQ
    on users (login);

create unique index USERS_EMAIL_UNQ
    on users (email);


CREATE TABLE friendship (
                            user_id int8 PRIMARY KEY,
                            friend_id int8,
                            status_id binary,
                            constraint USER_INFO_KEY
                            foreign key (user_id) references users(id),
                            constraint FRIEND_INFO_KEY
                            foreign key (friend_id) references users(id),
                            constraint STATUS_KEY
                            foreign key (status_id) references status (status_id)
);

CREATE TABLE likes (
                       film_id int8 PRIMARY KEY,
                       user_id int8,
                       constraint FILMS_KEY
                       foreign key (film_id) references films(id),
                       constraint USERS_KEY
                       foreign key (user_id) references users(id)
);