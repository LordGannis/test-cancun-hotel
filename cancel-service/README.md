# Cancun Hotel - Cancel Service

## MENU
* 1 - Introduction
* 2 - Service Goals
* 3 - Service Description
* 4 - Service Configuration
* 5 - Exceptions Handling

# 1. Introduction
The Cancel Service from the Cancun Hotel is the service with the purpose to cancel an existing booking.

# 2. Service Goals
We expect from the service:
* Cancel an existing booking pointed by the user;
* Handle exceptions properly and avoid explicit errors return.

# 3. Service Description
To fulfill it's purpose of cancel a booking for the user, the service:
* Provide a GET service under the endpoint *'/cancelbook/{bookId}'*;
* Change book in the table *tb_book* setting *active* to *false*.

# 4. Service Configuration
The service was made using Spring Boot (for easier configuration and inbound server), Spring Web (for the service expose) and Spring Data (for database manipulation). It also has direct connection with the Eureka Server as it need to be part for supply the Gateway. The service is covered by Unit Tests (JUnit, Mockito and H2 Database).

Other technologies used on it was: swagger and lombok.

The server points by default to the port *8400* and looks for the Eureka Server under *http://localhost:8050/eureka/*.

# 5. Exceptions Handling 
If the *bookId* informed can't find any book, the service need throw the exception BookNotFoundException, return error *404* and message *'Book not found for ID={id}'*.

For any unexpected exception, service need return error *500* and message *'There was a internal error to complete the request'*.