ForoHub

ForoHub is a Spring Boot-based RESTful API for managing forum topics and user authentication. It supports CRUD operations on topics, JWT-based security, and includes comprehensive tests and API documentation via OpenAPI.

Table of Contents

Prerequisites

Project Setup

Configuration

Database Migrations

Running the Application

API Documentation

Authentication

Endpoints

Authentication Endpoints

Topic Endpoints

Testing

Packaging

Contributing

License

Prerequisites

Before you begin, ensure you have the following installed on your machine:

Java Development Kit (JDK) 17 or higher

Verify with:

java -version

Maven 3.8+

Verify with:

mvn -v

MySQL Server 8+

Ensure you can connect with a user that has permissions to create databases.

Git

Verify with:

git --version

IDE of your choice

IntelliJ IDEA, Eclipse, VS Code, etc.

Project Setup

Clone the repository

git clone https://github.com/<your-username>/forohub.git
cd forohub

Create the database
Log in to MySQL and run:

CREATE DATABASE forohub_db;

Flyway Migrations
The project uses Flyway for database versioning. By default, Flyway runs automatically on startup and applies scripts in src/main/resources/db/migration.

Configuration

Application settings are in src/main/resources/application.properties (or application.yml). Update the following properties:

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/forohub_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

# Hibernate (optional if using Flyway)
spring.jpa.hibernate.ddl-auto=validate

# JWT Security\ nsecurity.jwt.secret=YourJWTSecretKeyHere
security.jwt.expiration-ms=3600000  # 1 hour in milliseconds

# OpenAPI (Swagger)
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

Tip: Never commit your real secrets to version control. Use environment variables or a vault for production.

Database Migrations

Location: src/main/resources/db/migration

Scripts: Start with V1__create_topics_table.sql, and add new migrations as you evolve the schema.

Running: Migrations run automatically on application startup.

Running the Application

Build with Maven

mvn clean install

Run

mvn spring-boot:run

Access

The API listens on http://localhost:8080

API Documentation

Once the application is running, Swagger UI is available at:

http://localhost:8080/swagger-ui.html

Use this interface to explore endpoints, request/response schemas, and try them out interactively.

Authentication

This API uses JWT for stateless authentication.

Register

Endpoint: POST /auth/register

Payload:

{
  "nombre": "Your Name",
  "email": "user@example.com",
  "password": "strongPassword"
}

Response: 201 Created with message.

Login

Endpoint: POST /auth/login

Payload:

{
  "email": "user@example.com",
  "password": "strongPassword"
}

Response: 200 OK with JSON { "token": "<JWT_TOKEN>" }

Using the Token

For secured endpoints, add the header:

Authorization: Bearer <JWT_TOKEN>

Endpoints

Authentication Endpoints

Method

Path

Description

POST

/auth/register

Register a new user

POST

/auth/login

Authenticate and get JWT

Topic Endpoints

Method

Path

Description

POST

/topics

Create a new topic (ROLE_USER)

GET

/topics

List all topics (paginated) (ROLE_USER)

GET

/topics/{id}

Get topic by ID (ROLE_USER)

PUT

/topics/{id}

Update topic by ID (ROLE_USER)

DELETE

/topics/{id}

Delete topic by ID (ROLE_USER)

Note: Endpoints under /moderator/** require ROLE_MODERATOR, and /admin/** require ROLE_ADMIN.

Testing

The project includes unit and integration tests.

Run all tests

mvn test

Test reports

Generated under target/surefire-reports.

Packaging

Package the application as a standalone JAR:

mvn clean package

Run the JAR:

java -jar target/forohub-1.0.0.jar

Contributing

Fork the repository.

Create a feature branch: git checkout -b feature/your-feature.

Commit your changes: git commit -m "feat: add your feature".

Push to your branch: git push origin feature/your-feature.

Open a Pull Request.

Please follow the existing code style and add tests for new functionality.

License

This project is licensed under the MIT License.

