-- Active: 1758333958128@@127.0.0.1@5432@spring_chatdb
CREATE DATABASE spring_chatdb;

CREATE TABLE  app_user(
  id       VARCHAR(100) NOT NULL,
  username VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE chat_room(
  id       VARCHAR(100) NOT NULL,
  name     VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);