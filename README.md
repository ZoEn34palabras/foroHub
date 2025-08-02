# ForoHub

ForoHub is a Spring Boot–based RESTful API for managing forum topics and user authentication.  
It supports CRUD operations on topics, JWT-based security, comprehensive tests, and API documentation via OpenAPI (Swagger).

---

## Table of Contents

1. [Prerequisites](#prerequisites)  
2. [Project Setup](#project-setup)  
3. [Configuration](#configuration)  
4. [Database Migrations](#database-migrations)  
5. [Running the Application](#running-the-application)  
6. [API Documentation](#api-documentation)  
7. [Authentication](#authentication)  
8. [Endpoints](#endpoints)  
   - [Authentication Endpoints](#authentication-endpoints)  
   - [Topic Endpoints](#topic-endpoints)  
9. [Testing](#testing)  
10. [Packaging](#packaging)  
11. [Contributing](#contributing)  
12. [License](#license)  

---

## Prerequisites

Make sure you have these installed:

- **Java 17+**  
  ```bash
  java -version
  ```
- **Maven 3.8+**  
  ```bash
  mvn -v
  ```
- **MySQL 8+**    
  You’ll need a user with permission to create databases.
- **Git**  
  ```bash
  git --version
  ```
- **IDE** (IntelliJ, Eclipse, VS Code, etc.)

---

## Project Setup

1. **Clone the repo**  
   ```bash
   git clone https://github.com/<your-username>/forohub.git
   cd forohub
   ```

2. **Create the database**  
   ```sql
   CREATE DATABASE forohub_db;
   ```

3. **Flyway migrations**  
   On startup, Flyway will automatically apply any SQL scripts in  
   `src/main/resources/db/migration`

---

## Configuration

Edit `src/main/resources/application.properties` (or `.yml`) with your settings:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/forohub_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

# Hibernate (optional with Flyway)
spring.jpa.hibernate.ddl-auto=validate

# JWT Security
security.jwt.secret=YourJWTSecretKeyHere
security.jwt.expiration-ms=3600000

# OpenAPI (Swagger)
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

> **Tip:** Don’t commit real secrets—use environment variables or a vault for production.

---

## Database Migrations

- **Location:** `src/main/resources/db/migration`  
- **Naming:** `V1__create_topics_table.sql`, `V2__…`, etc.  
- **Run:** Automatically on application startup.

---

## Running the Application

1. **Build**  
   ```bash
   mvn clean install
   ```

2. **Run**  
   ```bash
   mvn spring-boot:run
   ```
3. **Access**  
   Open `http://localhost:8080`

---

## API Documentation

Once running, explore Swagger UI at:  
```
http://localhost:8080/swagger-ui.html
```

---

## Authentication

This API uses JWT for stateless auth.

### Register
```
POST /auth/register
```
**Payload**
```json
{
  "nombre": "Your Name",
  "email": "user@example.com",
  "password": "strongPassword"
}
```
**Response**  
`201 Created` with a success message.

### Login
```
POST /auth/login
```
**Payload**
```json
{
  "email": "user@example.com",
  "password": "strongPassword"
}
```
**Response**  
`200 OK`  
```json
{ "token": "<JWT_TOKEN>" }
```

**Usage**  
For secured endpoints, include header:  
```
Authorization: Bearer <JWT_TOKEN>
```

---

## Endpoints

### Authentication Endpoints

| Method | Path             | Description                |
| ------ | ---------------- | -------------------------- |
| POST   | `/auth/register` | Register a new user        |
| POST   | `/auth/login`    | Authenticate and get a JWT |

### Topic Endpoints

| Method | Path             | Description                              |
| ------ | ---------------- | ---------------------------------------- |
| POST   | `/topics`        | Create a new topic (ROLE_USER)           |
| GET    | `/topics`        | List all topics (paginated) (ROLE_USER)  |
| GET    | `/topics/{id}`   | Get topic by ID (ROLE_USER)              |
| PUT    | `/topics/{id}`   | Update topic (ROLE_USER)                 |
| DELETE | `/topics/{id}`   | Delete topic (ROLE_USER)                 |

> **Note:**  
> - `/moderator/**` endpoints require `ROLE_MODERATOR`  
> - `/admin/**` endpoints require `ROLE_ADMIN`

---

## Testing

Run all tests with:
```bash
mvn test
```
Reports: `target/surefire-reports`

---

## Packaging

Build a standalone JAR:
```bash
mvn clean package
```
Run the JAR:
```bash
java -jar target/forohub-1.0.0.jar
```

---

## Contributing

1. Fork the repo  
2. Create a branch:  
   ```bash
   git checkout -b feature/your-feature
   ```
3. Make changes and commit:  
   ```bash
   git commit -m "feat: describe your feature"
   ```
4. Push to your branch:  
   ```bash
   git push origin feature/your-feature
   ```
5. Open a Pull Request

Follow existing code style and add tests for new functionality.

---

## License

This project is licensed under the [MIT License](LICENSE).
