# Graduation Project Backend

AI-supported learning platform backend for high-school education, built with Java and Spring Boot.

## Overview
The backend provides the core REST API for a learning platform with separate student and teacher experiences. It manages courses, sections, videos, materials, assessments, student progress, notes, feedback, notifications, carts, and wishlists.

## Tech Stack
- Java
- Spring Boot
- Spring Security
- JWT authentication
- Spring Data JPA / Hibernate
- PostgreSQL / H2
- Maven
- RESTful APIs

## Key Backend Areas
- Student and teacher authentication and authorization
- Course and category management
- Sections, videos, and learning materials
- Quizzes and assignment submissions
- Student progress tracking
- Notes, comments, ratings, and feedback
- Cart and wishlist functionality
- Notifications
- Validation and centralized exception handling

## Security
Secrets and environment-specific credentials are loaded through environment variables. Do not commit real credentials, database dumps, or local configuration.

## Configuration
Set the required environment variables before running the application. See `.env.example` for the expected configuration.

## Run Locally
```bash
./mvnw spring-boot:run
```

On Windows:
```powershell
.mvnw.cmd spring-boot:run
```

The application runs on port 8080 by default.

## Project Structure
The code follows a layered Spring architecture with controllers, services, repositories, entities, configuration, security, DTOs, validation, and exception handling.

## Status
Active graduation-project backend and portfolio project.
