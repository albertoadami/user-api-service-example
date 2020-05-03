CREATE TABLE users(
  id   SERIAL PRIMARY KEY,
  firstname VARCHAR NOT NULL,
  lastname VARCHAR NOT NULL,
  email  VARCHAR NOT NULL UNIQUE,
  password VARCHAR NOT NULL,
  birthday_date  VARCHAR NOT NULL,
  gender VARCHAR NOT NULL,
  creation_date VARCHAR NOT NULL,
  enabled boolean NOT NULL
);