INSERT INTO tb_user (id, name, login, creationdate) values
(1, 'Sophia Jackson', 'sophia_jackson', now()),
(2, 'Emma Black', 'emma_black', now()),
(3, 'Aiden Smith', 'asmith', now());

INSERT INTO tb_room (id, name, creationdate) values
(1, 'Master room', now());

INSERT INTO tb_book (id, room_id, user_id, startdate, enddate, creationdate, modificationdate, active) values
(100, 1, 1, '2021-07-01', '2021-07-03', now(), null, true),
(101, 1, 2, '2021-07-11', '2021-07-12', now(), null, true),
(102, 1, 1, '2021-06-26', '2021-06-26', now(), null, true);