# Cancun Hotel - Eureka Server

## MENU
* 1 - Introduction
* 2 - Server Goals
* 3 - Server Description
* 4 - Server Configuration

# 1. Introduction
The Eureka Server from the Cancun Hotel is responsible to know all instances of a microservice is running and under which port.

# 2. Server Goals
We expect from the service:
* Group servers (URLs/Ports) by name;
* Accept registration of new server;
* Handle exceptions properly and avoid explicit errors return.

# 3. Server Description
The Eureka Server will identify the servers as soon as they are open and disparing the connection to the server.

# 4. Server Configuration
The service was made using Spring Boot (for easier configuration and inbound server) and Eureka Server (Spring Clod API).

The server points by default to the port *8050* and it can be found under *http://localhost:8050/eureka/*.
