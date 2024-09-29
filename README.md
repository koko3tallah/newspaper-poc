
# Newspaper Management System

## Overview

The **Newspaper Management System** is a backend API service that manages user authentication, news creation, approval workflows, and user management. The project uses **Spring Boot** for rapid application development, **JWT** for secure authentication, and **MySQL** for persistent storage. The service supports role-based access control for three main roles: `USER`, `CONTENT_WRITER`, and `ADMIN`.

## Features

1. **Authentication**:
    - Login with JWT token issuance.
    - Secure token refresh mechanism.
    - Role-based access control.

2. **User Management**:
    - User signup, CRUD operations for users.
    - Password encryption using BCrypt.
    - Admin can create, update, and delete users.

3. **News Management**:
    - News CRUD operations.
    - Pending approval mechanism for news created by content writers.
    - Auto-soft deletion of news after exceeding the publishing date.

4. **Security**:
    - JWT-based authentication with token expiry and refresh support.
    - Role-based authorization for secure API access.

## Prerequisites

- Java 11+
- MySQL Server
- Maven (or any build tool of choice)

## Setup

### 1. Database Setup

Before running the application, you need to create the MySQL database and the user. You can run the following SQL script to set up the database:

```sql
-- Create the MySQL user
CREATE USER 'newspaper_admin' IDENTIFIED BY 'P@ssw0rd';

-- Create the schema
CREATE SCHEMA newspaper;

-- Grant privileges
GRANT ALL PRIVILEGES ON newspaper.* TO 'newspaper_admin'@'%';

-- After the application creates the 'user' table please run this insert statement.
-- Password for 'admin@gmail.com' is 'password123' (BCrypt hashed)
INSERT INTO newspaper.user (created_at, modified_at, date_of_birth, email, full_name, password, role, created_by_id, modified_by_id)
VALUES (null, null, '1990-01-01', 'admin@gmail.com', 'System Admin',
        '$2a$10$zDKaUgSFGHvwUuJo4hc1sOMTET.30U0ziOfaeYiokLYcaQy4DTR8O', 'ADMIN', null, null);
```

### 2. Build and Run

After setting up the database and configuration, you can build and run the application using Maven:

```bash
mvn clean install
mvn spring-boot:run
```

The application will start and listen on the default port `8099`. You can access the API using Postman or any other HTTP client.

## API Documentation

The project is integrated with **Swagger UI** for API documentation. After starting the application, you can view the available API endpoints by navigating to:

```
http://localhost:8099/swagger-ui.html
```

Alternatively, the raw JSON documentation is available at:

```
http://localhost:8099/v3/api-docs
```

## Key API Endpoints

### Authentication:
- **POST** `/login`: Authenticate a user and receive a JWT.
- **POST** `/refresh-token`: Refresh the access token using a refresh token.
- **POST** `/logout`: Invalidate the user’s token and log them out.

### User Management:
- **POST** `/v1/users`: Create a new user (Admin-only).
- **GET** `/v1/users`: Retrieve a list of all users (Admin-only).
- **GET** `/v1/users/{id}`: Get a specific user by ID (Admin-only).
- **PUT** `/v1/users/{id}`: Update a user’s details (Admin-only).
- **DELETE** `/v1/users/{id}`: Delete a user (Admin-only).

### News Management:
- **POST** `/v1/news`: Create a news article (Content Writer or Admin).
- **PUT** `/v1/news/{newsId}/approve`: Approve a pending news article (Admin-only).
- **DELETE** `/v1/news/{newsId}`: Request to delete a news article (Content Writer for pending news, Admin for approved news).
- **GET** `/v1/news`: Retrieve all approved news articles.
- **GET** `/v1/news/{newsId}`: Retrieve a specific approved news article.

## Testing

Unit and integration tests can be implemented using **JUnit** and **Mockito**.

### Running Tests:

```bash
mvn test
```

## Potential Improvements

Here are a few ways you can further enhance the project:

1. **Custom Exceptions**:
    - Implement custom exception classes for various error scenarios (e.g., `UserNotFoundException`, `InvalidTokenException`).

2. **Unit Testing**:
    - Write unit tests for controller, service, and repository layers using **JUnit** and **Mockito**.
    - Test API endpoints using integration testing frameworks like **Spring Boot Test**.

3. **Caching**:
    - Introduce caching mechanisms like **Redis** or **EhCache** to improve performance for frequently accessed resources like news articles.

4. **Rate Limiting**:
    - Implement rate limiting to protect APIs from abuse, ensuring secure and fair usage of resources.

5. **Email Verification**:
    - Add email verification during the signup process to ensure the validity of user accounts.
## Conclusion

This project demonstrates the core functionality of a newspaper management system, with user authentication, news workflows, and role-based access control. By following the suggested improvements, the system can be further enhanced in terms of security, performance, and maintainability.

---