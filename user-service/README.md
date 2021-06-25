# Cancun Hotel - User Service

## MENU
* 1 - Introduction
* 2 - Service Goals
* 3 - Service Description
* 4 - Service Configuration
* 5 - Exceptions Handling

# 1. Introduction
The User Service from the Cancun Hotel is the service with the purpose to find an user by his login.

# 2. Service Goals
We expect from the service:
* Find the user by the field *login* in the table *tb_user*;
* Handle exceptions properly and avoid explicit errors return.

# 3. Service Description
To fulfill it's purpose of finding an user, the service:
* Provide a GET service under the endpoint *'/user/{login}'*;
* Return the found user.

# 4. Service Configuration
The service was made using Spring Boot (for easier configuration and inbound server), Spring Web (for the service expose) and Spring Data (for database manipulation). It also has direct connection with the Eureka Server as it need to be part for supply the Gateway. The service is covered by Unit Tests (JUnit, Mockito and H2 Database).

Other technologies used on it was: swagger and lombok.

The server points by default to the port *8100* and looks for the Eureka Server under *http://localhost:8050/eureka/*.

# 5. Exceptions Handling 
If the *login* informed can't find any user, the service need throw the exception UserNotFoundException, return error *404* and message *'User Not Found for login={login}'*;

For any unexpected exception, service need return error *500* and message *'There was a internal error to complete the request'*.