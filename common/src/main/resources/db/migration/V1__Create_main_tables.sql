CREATE TABLE PUBLIC.member
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  login VARCHAR2(100) NOT NULL,
  email VARCHAR2(250),
  firstname VARCHAR2(100),
  lastname VARCHAR2(100),
  company VARCHAR2(100),
  ticketingRegistered BOOLEAN,
  registeredAt TIMESTAMP,
  shortDescription CLOB,
  longDescription CLOB,
  nbConsults BIGINT,
  publicProfile BOOLEAN  DEFAULT false NOT NULL
);
ALTER TABLE PUBLIC.member ADD CONSTRAINT member_unique UNIQUE (login);
