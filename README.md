RestApi endpoints
Assignment
Create new Spring Boot project using Initializer with dependencies:
Spring Web (Build web, including RESTful, applications using Spring MVC. Uses Apache Tomcat as the default embedded container.)
Spring Data JPA (Persist data in SQL stores with Java Persistence API using Spring Data and Hibernate.)
Flyway Migration (Version control for your database so you can migrate from any version (incl. an empty database) to the latest version of the schema.)
H2 Database or PostgreSQL Driver of your choice
Create model and schema initalizing sql migration script according with your UML diagrama
Create JPA repositories and service layer with base CRUD operations
Following best practices on RestAPI design - implement required endpoints to manage API model
Implement create/update/list/delete operations for provided data
manufacturers
manufacturers/model
manufacturers/model/year
ex: `POST /api/v1/manufacturers/toyota/models/corolla/2001`
Implement search endpoint with parameters like `manufacturer` ,'model', 'minYear', 'maxYear, 'category'
ex: `GET /api/v1/cars?manufacturer=mercedes&minYear=2005`
all list endpoints should support pagination and sorting
GET requests accessible for all users, POST requests - only authorized users.
Cover controllers with tests
Add additional components tests if required