# Garage Management Service

A robust and scalable Spring Boot application for managing garage operations, built with modern Java technologies and best practices.

## 🚀 Technologies & Tools

- **Java 17** - Latest LTS version with modern language features
- **Spring Boot 3.4.3** - Latest stable version with enhanced performance
- **Spring Security** - JWT-based authentication and authorization
- **Spring Data JPA** - Data access layer with PostgreSQL
- **Spring WebFlux** - Reactive programming support for non-blocking operations
- **PostgreSQL** - Robust relational database
- **Gradle** - Modern build system with dependency management
- **Docker** - Containerization for consistent deployment
- **Springdoc OpenAPI 2.8.5** - API documentation with Swagger UI
- **MapStruct 1.6.3** - Type-safe object mapping
- **Lombok** - Reduces boilerplate code
- **JWT (JJWT 0.11.5)** - JSON Web Token implementation
- **HikariCP 6.2.1** - High-performance connection pooling
- **JUnit 5** - Modern testing framework
- **Spring AOP** - Aspect-oriented programming support

## 📋 Prerequisites

- **JDK 17** or later
- **Gradle 8.x** or later
- **PostgreSQL 12** or later
- **Docker** (optional, for containerization)

## 🛠️ Getting Started

### Local Development Setup

1. **Clone the repository:**
   ```bash
   git clone [repository-url]
   cd garage-management-service
   ```

2. **Configure PostgreSQL:**
   - Create a database named `garage_management`
   - Update `application.properties` or `application.yml` with your database credentials:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/garage_management
       username: your_username
       password: your_password
   ```

3. **Build the project:**
   ```bash
   ./gradlew build
   ```

4. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

   The application will start on `http://localhost:8080`

### Docker Setup

1. **Build the Docker image:**
   ```bash
   docker build -t garage-management-service .
   ```

2. **Run the container:**
   ```bash
   docker run -p 8080:8080 garage-management-service
   ```

## 🔧 Configuration

The application can be configured through `application.properties` or `application.yml`. Key configurations include:

### Database Configuration
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/garage_management
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

### JWT Security Configuration
```yaml
jwt:
  secret: your-jwt-secret-key
  expiration: 86400000 # 24 hours
```

### Server Configuration
```yaml
server:
  port: 8080
  servlet:
    context-path: /api
```

## 📚 API Documentation

Once the application is running, you can access the API documentation at:
```
http://localhost:8080/swagger-ui.html
```

The OpenAPI specification is available at:
```
http://localhost:8080/v3/api-docs
```

## 🏗️ Project Structure

```
src/main/java/vn/utc/service/
├── config/          # Configuration classes (Security, Database, etc.)
├── controller/      # REST API controllers
├── dtos/           # Data Transfer Objects
├── entity/         # JPA entities
├── exception/      # Custom exception handlers
├── mapper/         # MapStruct mappers
├── repo/           # Repository interfaces
├── service/        # Business logic layer
└── GarageManagementServiceApplication.java  # Main application class
```

## 🔐 Security Features

The application implements comprehensive security using Spring Security:

- **JWT Authentication** - Stateless token-based authentication
- **Role-based Access Control** - Fine-grained authorization
- **Password Encryption** - BCrypt password hashing
- **CORS Configuration** - Cross-origin resource sharing
- **Secure Headers** - Security headers for protection
- **Input Validation** - Bean validation for data integrity

## 🧪 Testing

The project includes comprehensive test coverage:

- **Unit Tests** - Using JUnit 5 and Mockito
- **Integration Tests** - Spring Boot Test with TestContainers
- **Security Tests** - Spring Security Test
- **Repository Tests** - Data layer testing

Run tests using:
```bash
./gradlew test
```

Run tests with coverage:
```bash
./gradlew test jacocoTestReport
```

## 📈 Monitoring and Observability

- **Spring Boot Actuator** - Application monitoring and health checks
- **SLF4J with Logback** - Structured logging
- **Custom Logging Configuration** - Environment-specific logging
- **Health Check Endpoints** - `/actuator/health`
- **Metrics Endpoints** - `/actuator/metrics`

## 🚀 Performance Optimizations

- **HikariCP Connection Pooling** - High-performance database connections
- **Spring WebFlux** - Reactive programming for non-blocking operations
- **JPA Query Optimization** - Efficient database queries
- **Caching Support** - Spring Cache abstraction
- **Async Processing** - `@Async` for background tasks

## 🔄 Database Migrations

The application uses JPA's `ddl-auto: update` for automatic schema management. For production environments, consider using:

- **Flyway** - Database migration tool
- **Liquibase** - Alternative migration tool

## 🐳 Docker Support

The project includes a `Dockerfile` for containerization:

```dockerfile
FROM openjdk:17-jdk-slim
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Create a Pull Request

### Development Guidelines

- Follow Java coding conventions
- Write unit tests for new features
- Use meaningful commit messages
- Update documentation as needed
- Follow Spring Boot best practices

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- Your Name - Initial work

## 🙏 Acknowledgments

- Spring Boot Team for the excellent framework
- PostgreSQL Team for the robust database
- All contributors and maintainers
- Open source community for amazing tools and libraries

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation and API docs