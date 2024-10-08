# Application Overview

This application is built using the **Spring Boot** framework and follows a microservices-based architecture. It uses **PostgreSQL** for data storage, **Flyway** for database schema management, and integrates monitoring tools like **Prometheus** and **Grafana** for observability. The API documentation is exposed through **Swagger**.

## Key Technologies and Tools

- **Spring Boot**
- **PostgreSQL**
- **Flyway**
- **Swagger**
- **Prometheus**
- **Grafana**

## Database Migrations with Flyway

The application uses **Flyway** to manage database migrations. Flyway keeps track of the database schema versions and applies any pending migrations during startup, ensuring the database schema is always up-to-date.

## Compiling the Application

To compile the application, you need to have **Maven** installed on your machine.

### Steps to compile the Application:

1. Generate the JAR file using Maven:
   ```bash
   mvn clean package
   ```

## Running the Application

To run the application, you need to have **Docker** and **Docker Compose** installed on your machine.

### Steps to Run the Application:

1. Build the application using Docker Compose:
   ```bash
   docker-compose build
   ```

2. Start the application and all related services:
   ```bash
   docker-compose up
   ```

### Access the Application:

- **Endpoints**: The endpoints are exposed on the following URL:
    - [API](http://localhost:8080/)

- **API Documentation**: Once the application is running, you can access the Swagger UI to view and test the API endpoints:
    - [Swagger UI](http://localhost:8080/swagger-ui/index.html)

## Monitoring and Observability

The application is equipped with monitoring tools that help track its performance and health:

- **Database**: The application uses PostgreSQL as the database. You can access the database using the following credentials:
    - **Host**: `localhost`
    - **Port**: `5432`
    - **Database**: `sky_assessment`
    - **Username**: `postgres`
    - **Password**: `password`


- **Prometheus**: The Prometheus configuration can be found in the `prometheus.yml` file. Prometheus scrapes metrics from the application and stores them for monitoring purposes. Metrics provided by Spring actuator are consumed by Prometheus.
    - [Prometheus local instance](http://localhost:9090/)


- **Grafana**: Grafana dashboards are pre-configured to visualize metrics collected by Prometheus. You can access the Grafana UI at:
    - [Grafana Dashboard](http://localhost:3000/)

  The Grafana configuration is available in the `grafana` folder.

## Ports Used
The application uses the following ports:
- **8080**: Application port
- **5432**: PostgreSQL port
- **9090**: Prometheus port
- **3000**: Grafana port

## Postman Collection
The file `sky_assessment.postman_collection.json` contains a Postman collection with sample requests to test the API endpoints.