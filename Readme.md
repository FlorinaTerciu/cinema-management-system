# Cinema Management System
This project is designed as a full-stack learning application 
designed to showcase movie-booking experience system with Keycloak 
authentication, role-based access, movie reservations and scheduled 
cleanup jobs.

### Features
* Movie browsing & showtime scheduling
* Seat reservation & ticket purchasing
* Role-based access (CUSTOMER / EMPLOYEE)
* Automatic ticket expiration job
* JWT-based security with Keycloak
* Fully documented REST API via Swagger

### Roles & Capabilities
##### CUSTOMER
- Browse movies and showtimes
- View available seats
- Reserve, purchase and cancel tickets
- Manage own profile

##### EMPLOYEE
- Manage movies, rooms, seats and showtimes
- View showtime conflicts
- Finalize ticket purchases
- Manage users

###  API Documentation (Swagger)
All endpoints are documented via Swagger UI:

[API Documentation](http://localhost:8080/swagger-ui/index.html)

### Tech Stack

#### Backend
Java 21 <br>
Spring Boot 3.x <br>
Spring Web (REST) <br>
Spring Data JPA (Hibernate) <br>
Spring Security (OAuth2 Resource Server) <br>
MapStruct <br>
Jakarta Validation <br>
#### Security & Auth
Keycloak (JWT, Realm Roles) <br>
Roles: CUSTOMER, EMPLOYEE <br>
#### Database
PostgreSQL <br>
Dockerized <br>
Schema generated via JPA <br>
#### Infrastructure
Docker & Docker Compose
Maven
GitHub CI (build & test)

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.7/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.7/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.7/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.7/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Keycloak](https://www.keycloak.org/documentation)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

## Run with Docker

You only need one command:

docker compose up --build