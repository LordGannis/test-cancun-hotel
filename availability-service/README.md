# Cancun Hotel - Availability Service

## MENU
* 1 - Introduction
* 2 - Service Goals
* 3 - Service Description
* 4 - Service Configuration
* 5 - Exceptions Handling

# 1. Introduction
The Availability Service from the Cancun Hotel is the service with the purpose to return the current available days for reservation in the hotel.

# 2. Service Goals
We expect from the service:
* Return all dates available or none, if that is the case;
* Handle exceptions properly and avoid explicit errors return.

# 3. Service Description
The service expect to receive the ID for the room that is desired to check the availability.

To fulfill it's purpose, the service filters the available dates considering the follow rules:
* Provide a GET service under the endpoint *'/availability/{roomId}'*;
* Available dates are from 30 days ahead maximum; and
* If there is any book in *tb_book* with the field *active* equal *true* and *room_id* equal parameter *roomId*, for the day, it should not be considered.

# 4. Service Configuration
The service was made using Spring Boot (for easier configuration and inbound server), Spring Web (for the service expose) and Spring Data (for database manipulation). It also has direct connection with the Eureka Server as it need to be part for supply the Gateway. The service is covered by Unit Tests (JUnit, Mockito and H2 Database).

Other technologies used on it was: swagger and lombok.

The server points by default to the port *8200* and looks for the Eureka Server under *http://localhost:8050/eureka/*.

# 5. Exceptions Handling 
If the room's id informed can't find any room, the service need throw the exception RoomNotFoundException, return error *404* and message *'Room not found for ID={id}'*.

For any unexpected exception, service need return error *500* and message *'There was a internal error to complete the request'*.