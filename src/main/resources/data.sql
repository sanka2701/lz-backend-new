INSERT INTO user VALUES(1,'admin','password','testuser@lz.sk','first','last','2012-09-17','ROLE_ADMIN');

INSERT INTO tag VALUES(1,'Sport');
INSERT INTO tag VALUES(2,'Kultura');
INSERT INTO tag VALUES(3,'Deti');
INSERT INTO tag VALUES(4,'Alkohol');

INSERT INTO place VALUES (2, 1, 'label', 'address', 19.596746688751296, 49.09751301668884, true);

INSERT INTO file VALUES (1, 'jpg', '66f5b8ee8ce64c0dae888e0fa129d3cc', '2018\\12\\26');

INSERT INTO event VALUES (888, 1, 2, '<p>weq</p>', 1, 'heading', '2018-12-08', '2018-11-29', '05:15:00', '2018-12-13', '01:15:00', true);

INSERT INTO event_tag VALUES (888, 1);

INSERT INTO article VALUES(999, 1, '<p>weq</p>', 1, 'title', '2019-12-08', true)