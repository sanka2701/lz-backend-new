CREATE TABLE user (
  user_id        INTEGER  NOT NULL PRIMARY KEY AUTO_INCREMENT,
  username       VARCHAR(25),
  password       VARCHAR(25),
  email          VARCHAR(25),
  firstName      VARCHAR(25),
  lastName       VARCHAR(25),
  created_date   DATE,
  role           VARCHAR(25)
);

CREATE TABLE place (
  place_id   INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
  owner_id   INTEGER,
  label      VARCHAR(25),
  address    VARCHAR(25),
  longitude  DOUBLE,
  latitude   DOUBLE,
  approved   BOOL,
  FOREIGN KEY (owner_id) REFERENCES user (user_id)
);

CREATE TABLE file (
  file_id     INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
  extension   VARCHAR(5),
  name        VARCHAR(50),
  directory   VARCHAR(100)
);

CREATE TABLE tag (
  tag_id  INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
  label   VARCHAR(25)
);

CREATE TABLE event (
  event_id       INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
  owner_id       INTEGER NOT NULL,
  place_id       INTEGER NOT NULL,
  content        TEXT,
  thumbnail      INTEGER NOT NULL,
  heading        VARCHAR(50),
  date_added     DATE,
  start_date     DATE,
  start_time     TIME,
  end_date       DATE,
  end_time       TIME,
  approved       BOOL DEFAULT false,
  FOREIGN KEY (thumbnail) REFERENCES file (file_id),
  FOREIGN KEY (owner_id) REFERENCES user (user_id),
  FOREIGN KEY (place_id) REFERENCES place (place_id)
);

CREATE TABLE event_tag (
  event_id   INTEGER NOT NULL,
  tag_id     INTEGER NOT NULL,
  PRIMARY KEY (event_id,tag_id),
  KEY fk_event (event_id),
  KEY fk_tag (tag_id),
  CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES tag (tag_id),
  CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES event (event_id)
);

CREATE TABLE content_file (
  event_id   INTEGER NOT NULL,
  file_id     INTEGER NOT NULL,
  PRIMARY KEY (event_id,file_id),
  KEY fk_f_event (event_id),
  KEY fk_f_file (file_id),
  CONSTRAINT fk_f_file FOREIGN KEY (file_id) REFERENCES file (file_id),
  CONSTRAINT fk_f_event FOREIGN KEY (event_id) REFERENCES event (event_id)
);