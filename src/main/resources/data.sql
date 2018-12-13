insert into users values(1,'2012-09-17','testuser@lz.sk','first','last','password','ROLE_ADMIN','admin');
insert into users values(2,'2017-01-11','jozo@lz.sk','jozo','spity','password','ROLE_USER','jozou');
insert into users values(3,'2017-05-20','fero@lz.sk','fero','prdel','password','ROLE_USER','ferou');
insert into users values(4,'2018-10-07','sebastian@lz.sk','sebo','vypil','password','ROLE_USER','sebou');
insert into users values(5,'2018-11-22','bororo@lz.sk','borak','zabludil','password','ROLE_USER','bororou');

insert into places values(1,'Nejaka adresa 8',true,'Nejaky label',19.596746688751296,49.09751301668884,1);

-- insert into posts values(1,'<p>weq</p>','2018-12-13',1,'http://localhost:8080/img/2018/11/13/3e663056985e4ddba98d6d12ee36f79f.jpg','qwe');
-- insert into events (id, place, approved, end_date, end_time, start_date, start_time) values(1, 1, true,'2018-12-08','05:15:00','2018-11-29','01:15:00');

insert into tags values(1,'Sport');
insert into tags values(2,'Kultura');
insert into tags values(3,'Deti');
insert into tags values(4,'Alkohol');

-- insert into event_tags (event_id, tag_id) values(1,2);
-- insert into event_tags (event_id, tag_id) values(1,4);