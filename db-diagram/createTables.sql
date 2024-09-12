CREATE TABLE "friendship" (
  "user1_id" integer,
  "user2_id" integer,
  "confirmed" bool,
  PRIMARY KEY ("user1_id", "user2_id")
);

CREATE TABLE "users" (
  "id" integer PRIMARY KEY,
  "email" varchar,
  "login" varchar,
  "user_name" varchar,
  "birthday" date,
  "created_at" date
);

CREATE TABLE "genre" (
  "id" integer PRIMARY KEY,
  "name_genre" varchar
);

CREATE TABLE "film" (
  "id" integer PRIMARY KEY,
  "name_film" varchar,
  "description" varchar,
  "release_date" date,
  "duration" integer,
  "genre_id" integer,
  "rating" varchar
);

CREATE TABLE "film_like" (
  "film_id" integer,
  "user_id" integer,
  PRIMARY KEY ("film_id", "user_id")
);

CREATE TABLE "rating" (
  "name_rating" varchar PRIMARY KEY,
  "description" varchar
);

ALTER TABLE "friendship" ADD FOREIGN KEY ("user1_id") REFERENCES "users" ("id");

ALTER TABLE "friendship" ADD FOREIGN KEY ("user2_id") REFERENCES "users" ("id");

ALTER TABLE "film" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("id");

ALTER TABLE "film" ADD FOREIGN KEY ("rating") REFERENCES "rating" ("name_rating");

ALTER TABLE "film_like" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "film_like" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");
