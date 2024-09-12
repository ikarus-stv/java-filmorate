-- PUT /users/{id}/friends/{friendId} — добавление в друзья

INSERT INTO friendship(user1_id, user2_id)
VALUES (1,2);

-- DELETE /users/{id}/friends/{friendId} — удаление из друзей.

DELETE
FROM friendship
WHERE user1_id = 1
  AND user2_id = 2;

-- GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.

SELECT user2_id
FROM friendship
WHERE user1_id = 1
  AND confirmed
UNION
SELECT user1_id
FROM friendship
WHERE user2_id = 1
  AND confirmed
ORDER BY 1;

-- GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.

SELECT user2_id
FROM
  (SELECT user2_id
   FROM friendship
   WHERE user1_id = 1
     AND confirmed
   UNION SELECT user1_id
   FROM friendship
   WHERE user2_id = 1
     AND confirmed) A
WHERE A.user2_id IN
    (SELECT user2_id
     FROM friendship
     WHERE user1_id = 2
       AND confirmed
     UNION SELECT user1_id
     FROM friendship
     WHERE user2_id = 2
       AND confirmed)
ORDER BY 1;

-- PUT /films/{id}/like/{userId}

INSERT INTO film_like (film_id, user_id)
VALUES (13,10);

-- DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.

DELETE
FROM film_like
WHERE film_id = 13
  AND user_id =10;

-- GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано, верните первые 10

SELECT id,
       name_film,
       description,
       release_date,
       duration,
       genre_id,
       rating,
       count(fl.film_id) cnt
FROM film f
LEFT OUTER JOIN film_like fl ON f.id = fl.film_id
GROUP BY id,
         name_film,
         description,
         release_date,
         duration,
         genre_id,
         rating
ORDER BY cnt DESC,
         f.name_film
LIMIT 10;