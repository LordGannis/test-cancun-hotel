# Cancun Hotel - Gateway Service

## MENU
* 1 - Introduction
* 2 - Service Goals
* 3 - Service Description
* 4 - Service Configuration

# 1. Introduction
The Gateway Service from the Cancun Hotel is responsible to ready the services and their instances and do the redicted/load balancing to them.

# 2. Service Goals
We expect from the service:
* Transparency to fetch the services;
* Handle exceptions properly and avoid explicit errors return.

# 3. Service Description
The Gateway Service will identify all the services running and provide a load balance to them. 

# 4. Service Configuration
The service was made using Spring Boot (for easier configuration and inbound server) and Eureka Server (Spring Clod API).

The server points by default to the port *8070* and it swagger with all services can be found under *http://localhost:8070/swagger-ui/*.

For each service, it is necessary add them to the application.yml. Here is a sample:
```
- id: modifybook-service // ID from the service
  uri: lb://modifybook-service // URL fomr the serve (here uwe use 'lb' for loadbance 
  predicates:
    - Path=/modifybook/**
```

