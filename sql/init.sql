-- UP
CREATE DATABASE IF NOT EXISTS library;

DROP TABLE IF EXISTS library.book;

CREATE TABLE IF NOT EXISTS library.book (
  id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  title TEXT NOT NULL,
  author TEXT NOT NULL,
  created DATETIME
);

TRUNCATE library.book;

INSERT INTO library.book (id, title, author, created) VALUES (1, 'Ancillary Mercy', 'Ann Leckie', NOW());
INSERT INTO library.book (id, title, author, created) VALUES (2, 'N. K. Jemisin','The Fifth Season', NOW());
INSERT INTO library.book (id, title, author, created) VALUES (3, 'Ken Liu', 'The Grace of Kings', NOW());
INSERT INTO library.book (id, title, author, created) VALUES (4, 'Charles E. Gannon', 'Raising Caine', NOW());
INSERT INTO library.book (id, title, author, created) VALUES (5, 'Fran Wilde', 'Updraft', NOW());