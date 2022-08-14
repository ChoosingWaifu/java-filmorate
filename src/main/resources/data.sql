MERGE into STATUS(status_id, status)
values ( 0,'Unconfirmed' ),
       (1,'Confirmed');

MERGE into MPA(mpa_id, name)
values (1,'G'),
       (2,'PG'),
       (3,'PG-13'),
       (4,'R'),
       (5,'NC-17');

MERGE into GENRES(genre_id, name)
values (1,'Комедия'),
       (2,'Драма'),
       (3,'Мультфильм'),
       (4,'Триллер'),
       (5,'Документальный'),
       (6,'Боевик');