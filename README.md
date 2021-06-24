# Cancun Hotel

## MENU
* 1 - Introduction
* 2 - Acceptance Criterias
* 3 - Services Integration
* 4 - Database 
* 5 - Scripts Database
* 6 - Front-end side

# 1. Introduction
Cancun Hotel is a back-end aggregation of microservices with the goal to attend to a book system for the Cancun Hotel.
Currently the hotel has only one room available!

# 2. Acceptance Criterias
We expect from the system:
* Avoid downtimes;
* Allow user to see his bookings;
* Allow user to see available days for bookings;
* Allow user to book an available range of days from one to up to three days;
* Allow user to modify his current bookings; and
* Allow user to cancel his current bookings.

*One additional rule that would be good to be added, but at this time it wasn't implemented, is to limit the reservations per user. Each reservation can have up to three days, however, the user can make N reservations currently.*

# 3. Services Integration

In order to look for a better service flow and avoid the downtimes, we implemented a microservices arquitecture. To make it simpler, we have a "big project" called cancun-hotel-parent, which one is aggregation other eight different projects.

The microservice architecture was chosen because you can have multiple services of the same project running, and, if eventually one come down, you have other services to supply the data while you fix this one. In order to everything works fine, we need a naming server and load balance for the different services instances.  

This arquitecture works as you can see in the image below:
![Image of Yaktocat](https://raw.githubusercontent.com/guilhermepigosso/test-cancun-hotel/master/docs/diagrams/services_integration.png)

Explaining the projects:
* Eureka Server: a cloud system due with Spring Cloud and the Spring Cloud Netflix API Eureka, this is our naming server and works to determine the URLs for each service and, later, will feed the Gateway with this information;
* Gateway Service: another Spring Cloud API, the Spring Cloud Gateway is a service that provides easier, simpler and automated way to fetch the services in their different instances. This services reads the Eureka Server to discovery the services and their URLs. In this way, the Gateway provides the services that each of his mapped systems provides, and make it transparent to the user, so the user don't have to know which is (or are!) the URL for the service. If desired, the Gateway can even rewrite the endpoints to the other side. Also, it provides the load balance between the services;
* User Service: a Spring Boot service that has the goal to provide the service to authenticate the user. This system connects directly to the database. Deeper definition of the service can be found in his README, at: user-service/README.md;
* Availability Service: a Spring Boot service that has the goal to provide the service to verify the available days for reservation. This system connects directly to the database. Deeper definition of the service can be found in his README, at: availability-service/README.md;
* Book Service: a Spring Boot service that has the goal to provide two services, one to create a new booking to the user and a second to fetch user's booking. *Usually microservices has only one goal, whoever, this service is providing two different because they are related and to speed up the development*. This system connects directly to the database. Deeper definition of the service can be found in his README, at: book-service/README.md;
* Cancel Service: a Spring Boot service that has the goal to provide to provide the service to cancel a user's booking. This system connects directly to the database. Deeper definition of the service can be found in his README, at: cancel-service/README.md;
* Modify Service: a Spring Boot service that has the goal to provide to provide the service to cancel a user's booking. This system connects directly to the database. Deeper definition of the service can be found in his README, at: cancel-service/README.md;
* Common Utils: a simple project that has the goal to provide common utilities/classes to the services. This system has no connection with the Eureka Server, Gateway or to the database. Deeper definition of the service can be found in his README, at: common-utils/README.md; and
* Database: a shared database between the services, that manage all the data needed for users and booking.

After run the services, it is possible to consume the services using the Gateway Swagger UI. It is by default in the URL: http://localhost:8070/swagger-ui/

# 4. Database 
The Database is a PostgreSQL. The connection can be done using:
* Host: ec2-35-172-85-250.compute-1.amazonaws.com
* Port: 5432
* Database: dasqijr3amabkk
* Username: tiqfhcwpfaluoy
* Password: 79562c6ca2490c84e3778f36f67fd4cf162b9c5e9179fd616882020cd9675d12

# 5. Scripts Database
The script to create the database was:
```
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
```

# 6. Front-end side

The front-end system was done in Vue.js and it connect to the Gateway Service, as expect, in order to use the services. More detailed information about it can be found under the project cancun-hotel-front/README.md

