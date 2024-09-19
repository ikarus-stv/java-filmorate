SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE rating;
TRUNCATE TABLE genre;
TRUNCATE TABLE FILM_GENRE;
TRUNCATE TABLE FILM_LIKE;

SET REFERENTIAL_INTEGRITY TRUE;


INSERT INTO rating (id,name_rating, description) VALUES
	 (1,'G','У фильма нет  возрастных ограничений'),
	 (2,'PG','детям рекомендуется смотреть фильм с родителями'),
	 (3, 'PG-13','детям до 13 лет просмотр не желателен'),
	 (4, 'R','лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
	 (5, 'NC-17','лицам до 18 лет просмотр запрещён');
ALTER TABLE rating ALTER COLUMN id RESTART WITH 6;

INSERT INTO genre (id,name_genre) VALUES
	 (1,'Комедия'),
	 (2,'Драма'),
	 (3,'Мультфильм'),
	 (4,'Триллер'),
	 (5,'Документальный'),
	 (6,'Боевик');
ALTER TABLE genre ALTER COLUMN id RESTART WITH 7;

INSERT INTO USERS (id, EMAIL , LOGIN , USER_NAME , BIRTHDAY)
VALUES (1, 'Capitan@yandex.ru', 'Capitan', 'Capitan', '2001-01-01'),   -- 1
    (2, 'Jack@yandex.ru', 'Jack', 'Jack', '2002-02-02'),       -- 2
    (3, 'Sparrow@yandex.ru', 'Sparrow', 'Sparrow', '2003-03-03');   -- 3
ALTER TABLE USERS ALTER COLUMN id RESTART WITH 4;

INSERT INTO film (id,name_film,description,release_date,duration,rating_id) VALUES
	 (1,'Они учатся в Москве',NULL,'1950-02-03',NULL,1),
	 (2,'Дорога имени Октября',NULL,'1951-02-03',NULL,2),
	 (3,'На первенство мира по шахматам',NULL,'1951-02-03',NULL,3),
	 (4,'Твои книжки',NULL,'1953-02-03',NULL,5),
	 (5,'Недалеко от Краснодара',NULL,'1953-02-03',NULL,4),
	 (6,'Остров Сахалин',NULL,'1954-02-03',NULL,1),
	 (7,'Весенние голоса',NULL,'1955-02-03',NULL,2),
	 (8,'В дальневосточных морях',NULL,'1956-02-03',NULL,3),
	 (9,'Карнавальная ночь',NULL,'1956-02-03',NULL,5),
	 (10,'Девушка без адреса',NULL,'1957-02-03',NULL,4);
INSERT INTO film (id,name_film,description,release_date,duration,rating_id) VALUES
	 (11,'Цель его жизни',NULL,'1957-02-03',NULL,1),
	 (12,'Совершенно серьёзно',NULL,'1961-02-03',NULL,2),
	 (13,'Человек ниоткуда',NULL,'1961-02-03',NULL,3),
	 (14,'Гусарская баллада',NULL,'1962-02-03',NULL,5),
	 (15,'Дайте жалобную книгу',NULL,'1964-02-03',NULL,4),
	 (16,'Берегись автомобиля',NULL,'1966-02-03',NULL,1),
	 (17,'Зигзаг удачи',NULL,'1968-02-03',NULL,2),
	 (18,'Один час с Козинцевым, или Семь мнений',NULL,'1970-02-03',NULL,3),
	 (19,'Старики-разбойники',NULL,'1971-02-03',NULL,5),
	 (20,'Невероятные приключения итальянцев в России',NULL,'1973-02-03',NULL,4);
INSERT INTO film (id,name_film,description,release_date,duration,rating_id) VALUES
	 (21,'Сослуживцы',NULL,'1973-02-03',NULL,1),
	 (22,'Ирония судьбы, или С лёгким паром!',NULL,'1975-02-03',NULL,2),
	 (23,'Риск — благородное дело',NULL,'1977-02-03',NULL,3),
	 (24,'Служебный роман',NULL,'1977-02-03',NULL,5),
	 (25,'Иван Пырьев',NULL,'1978-02-03',NULL,4),
	 (26,'Гараж',NULL,'1979-02-03',NULL,1),
	 (27,'Нужна хорошая мелодия',NULL,'1980-02-03',NULL,2),
	 (28,'О бедном гусаре замолвите слово',NULL,'1980-02-03',NULL,3),
	 (29,'Родственники',NULL,'1981-02-03',NULL,5),
	 (30,'Вокзал для двоих',NULL,'1982-02-03',NULL,4);
INSERT INTO film (id,name_film,description,release_date,duration,rating_id) VALUES
	 (31,'Жестокий романс',NULL,'1984-02-03',NULL,1),
	 (32,'Забытая мелодия для флейты',NULL,'1987-02-03',NULL,2),
	 (33,'Четыре встречи с Владимиром Высоцким',NULL,'1987-02-03',NULL,3),
	 (34,'Дорогая Елена Сергеевна',NULL,'1988-02-03',NULL,5),
	 (35,'Небеса обетованные',NULL,'1991-02-03',NULL,4),
	 (36,'День в семье президента',NULL,'1993-02-03',NULL,1),
	 (37,'Предсказание',NULL,'1993-02-03',NULL,2),
	 (38,'Я — легкомысленный грузин',NULL,'1994-02-03',NULL,3),
	 (39,'Артист совсем не то же, что актёр…',NULL,'1996-02-03',NULL,5),
	 (40,'Привет, дуралеи!',NULL,'1996-02-03',NULL,4);
INSERT INTO film (id,name_film,description,release_date,duration,rating_id) VALUES
	 (41,'Единица порядочности — один Галлай',NULL,'1997-02-03',NULL,1),
	 (42,'Старые клячи',NULL,'2000-02-03',NULL,2),
	 (43,'Тихие омуты',NULL,'2000-02-03',NULL,3),
	 (44,'Ключ от спальни',NULL,'2003-02-03',NULL,5),
	 (45,'Андерсен. Жизнь без любви',NULL,'2006-02-03',NULL,4),
	 (46,'Карнавальная ночь 2, или 50 лет спустя',NULL,'2006-02-03',NULL,1),
	 (47,'Ирония судьбы. Продолжение',NULL,'2007-02-03',NULL,2),
	 (48,'Музыка жизни',NULL,'2009-02-03',NULL,3);
ALTER TABLE FILM ALTER COLUMN id RESTART WITH 49;


insert into friendship(user1_id, user2_id, confirmed) values (1,2,true);
insert into friendship(user1_id, user2_id, confirmed) values (1,3,true);

INSERT INTO FILM_GENRE (FILM_ID,GENRE_ID) VALUES
	 (5,1),
	 (5,2),
	 (5,3),
	 (6,4),
	 (6,5),
	 (6,6);

INSERT INTO FILM_LIKE (FILM_ID,USER_ID) VALUES
	 (3,1),
	 (3,2),
	 (3,3);
