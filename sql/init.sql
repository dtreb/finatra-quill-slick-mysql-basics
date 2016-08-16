CREATE DATABASE IF NOT EXISTS circle_test;

DROP TABLE IF EXISTS circle_test.book;

CREATE TABLE IF NOT EXISTS circle_test.book (
  id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  title TEXT NOT NULL,
  author TEXT NOT NULL,
  created DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO circle_test.book (id, title, author, created) VALUES (1, 'Ancillary Mercy', 'Ann Leckie', NOW());
INSERT INTO circle_test.book (id, title, author, created) VALUES (2, 'N. K. Jemisin','The Fifth Season', NOW());
INSERT INTO circle_test.book (id, title, author, created) VALUES (3, 'Ken Liu', 'The Grace of Kings', NOW());
INSERT INTO circle_test.book (id, title, author, created) VALUES (4, 'Charles E. Gannon', 'Raising Caine', NOW());
INSERT INTO circle_test.book (id, title, author, created) VALUES (5, 'Fran Wilde', 'Updraft', NOW());