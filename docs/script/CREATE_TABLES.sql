CREATE SEQUENCE tb_room_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE tb_room (
	id int8 NOT NULL DEFAULT nextval('tb_room_seq'::regclass),
	name varchar(150) NOT NULL,
	creationdate timestamp NOT NULL,
	CONSTRAINT tb_room_pk PRIMARY KEY (id)
);

CREATE SEQUENCE tb_user_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE tb_user (
	id int8 NOT NULL DEFAULT nextval('tb_user_seq'::regclass),
	name varchar(150) NOT NULL,
	login varchar(50) NOT NULL,
	creationdate timestamp NOT NULL,
	CONSTRAINT tb_user_pk PRIMARY KEY (id)
);

CREATE SEQUENCE tb_book_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE tb_book (
	id int8 NOT NULL DEFAULT nextval('tb_book_seq'::regclass),
	room_id int8 NOT NULL,
	user_id int8 NOT NULL,
	startdate date NOT NULL,
	enddate date NOT NULL,
	active boolean NOT NULL DEFAULT true;
	creationdate timestamp NOT NULL,
	modificationdate timestamp NULL,
	CONSTRAINT tb_book_pk PRIMARY KEY (id),
	CONSTRAINT tb_book_tb_room_fk FOREIGN KEY (room_id) REFERENCES tb_room(id),
	CONSTRAINT tb_book_tb_user_fk FOREIGN KEY (user_id) REFERENCES tb_user(id)
);