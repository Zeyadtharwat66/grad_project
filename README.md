# AI Learning Platform

An AI-supported learning platform backend for high-school education, built with **Java** and **Spring Boot**.

The backend provides RESTful APIs for student and teacher workflows, including course management, learning content, assessments, student progress, and e-commerce features.

## 🚀 Technologies

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA / Hibernate
* PostgreSQL / H2
* Maven
* RESTful APIs

## 📁 Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── com/pos/grad_project/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       ├── entity/
│   │       ├── dto/
│   │       ├── security/
│   │       ├── config/
│   │       ├── exception/
│   │       └── validation/
│   │
│   └── resources/
│       └── application.properties
│
├── pom.xml
├── .env.example
└── README.md
```

## ⚙️ Installation

Clone the repository:

```bash
git clone https://github.com/Zeyadtharwat66/AI-Learning-Platform.git
```

Navigate to the project:

```bash
cd AI-Learning-Platform
```

Run the application using Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.mvnw.cmd spring-boot:run
```

## 🔐 Environment Variables

Create your environment configuration based on `.env.example`.

The project uses environment variables for database configuration and JWT settings.

Example:

```env
DB_URL=jdbc:h2:file:./data/grad_project
DB_DRIVER=org.h2.Driver
DB_USERNAME=zeyad
DB_PASSWORD=
DB_DIALECT=org.hibernate.dialect.H2Dialect
H2_CONSOLE_ENABLED=false
DEFAULT_USER_NAME=zeyad
DEFAULT_USER_PASSWORD=
JWT_SECRET=replace-with-a-long-random-secret
JWT_ISSUER=zeyad-tharwat
JWT_EXP_MINUTES=120
```

> **Note:** Do not commit real passwords, JWT secrets, database credentials, or database dumps.

## ▶️ Running the Project

The application runs on:

```text
http://localhost:8080
```

The default development configuration uses H2. The application can also be configured to use PostgreSQL through environment variables.

## 📌 Main Features

* Student and teacher authentication
* JWT-based authentication
* Course and category management
* Course sections and videos
* Learning materials
* Quizzes and assignment submissions
* Student progress tracking
* Notes and comments
* Ratings and feedback
* Cart and wishlist
* Notifications
* Request validation
* Centralized exception handling
* RESTful API architecture

## 🛠️ Backend Architecture

The project follows a layered Spring Boot architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

DTOs, validation, security, configuration, and exception handling are separated into dedicated packages.

## 🔮 Future Improvements

* AI-powered curriculum chatbot
* Video transcript and timestamp search
* Image and text search across learning videos
* Python-based AI service integration
* PostgreSQL with pgvector for AI-related search
* Cloud deployment

## 👨‍💻 Author

**Zeyad Tharwat**

## 📄 License

This project is a graduation project and is intended for learning and development purposes.
