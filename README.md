# Garage Management Service

A robust and scalable Spring Boot application for managing garage operations, built with modern Java technologies and best practices.

## 🚀 Technologies & Tools

- Java 17
- Spring Boot 3.4.3
- Spring Security with JWT Authentication
- Spring Data JPA
- PostgreSQL Database
- Gradle
- Docker
- Springdoc OpenAPI (Swagger)
- MapStruct
- Lombok
- JUnit 5
- Spring WebFlux (Reactive Programming Support)

## 📋 Prerequisites

- JDK 17 or later
- Gradle 8.x
- PostgreSQL 12 or later
- Docker (optional, for containerization)

## 🛠️ Getting Started

### Local Development Setup

1. Clone the repository:
   ```bash
   git clone [repository-url]
   cd garage-management-service
   ```

2. Configure PostgreSQL:
   - Create a database named `garage_management`
   - Update `application.properties` or `application.yml` with your database credentials

3. Build the project:
   ```bash
   ./gradlew build
   ```

4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

### Docker Setup

1. Build the Docker image:
   ```bash
   docker build -t garage-management-service .
   ```

2. Run the container:
   ```bash
   docker run -p 8080:8080 garage-management-service
   ```

## 🔧 Configuration

The application can be configured through `application.properties` or `application.yml`. Key configurations include:

- Database connection settings
- JWT security settings
- Server port and context path
- Logging levels
- Environment-specific properties

## 📚 API Documentation

Once the application is running, you can access the API documentation at:
```
http://localhost:8080/swagger-ui.html
```

## 🏗️ Project Structure

```
src/main/java/vn/utc/service/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
├── entity/         # JPA entities
├── repository/     # Data access layer
├── service/        # Business logic layer
├── security/       # Security configuration
└── util/           # Utility classes
```

## 🔐 Security

The application implements JWT-based authentication and authorization using Spring Security. Key security features include:

- JWT token-based authentication
- Role-based access control
- Password encryption
- CORS configuration
- Secure endpoints

## 🧪 Testing

The project includes comprehensive test coverage:

- Unit tests using JUnit 5
- Integration tests with Spring Boot Test
- Security tests
- Repository layer tests

Run tests using:
```bash
./gradlew test
```

## 📈 Monitoring and Logging

- Spring Boot Actuator for application monitoring
- SLF4J with Logback for logging
- Custom logging configuration
- Health check endpoints

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- Your Name - Initial work

## 🙏 Acknowledgments

- Spring Boot Team
- All contributors and maintainers