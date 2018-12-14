INSERT INTO user VALUES(1,'admin','password','testuser@lz.sk','first','last','2012-09-17','ROLE_ADMIN');

INSERT INTO tag VALUES(1,'Sport');
INSERT INTO tag VALUES(2,'Kultura');
INSERT INTO tag VALUES(3,'Deti');
INSERT INTO tag VALUES(4,'Alkohol');

INSERT INTO place VALUES (2, 1, 'label', 'address', 19.596746688751296, 49.09751301668884, true);

INSERT INTO event VALUES(1, 1, 2, '<p>weq</p>', 'http://localhost:8080/img/2018/11/13/3e663056985e4ddba98d6d12ee36f79f.jpg',
                         'heading', '2018-12-08', '2018-11-29', '05:15:00', '2018-12-13', '01:15:00', true);

INSERT INTO event_tag VALUES (1, 2);
INSERT INTO event_tag VALUES (1, 4);