# Gemini Project Configuration: kyc-mcp

This document provides project-specific context to the Gemini AI assistant. Adhering to these guidelines will ensure
consistency and leverage the existing patterns within the project.

## 1. Project Overview

This is a Spring Boot application designed for a KYC (Know Your Customer) system. It manages clients and their
associated individuals to facilitate compliance and risk assessment processes. The application exposes a REST API for
these operations and uses a relational database for persistence.

## 2. Key Technologies

- **Backend:** Java 24, Spring Boot 3.5
- **Data Persistence:** Spring Data JPA, Hibernate
- **Database:** PostgreSQL
- **Database Migration:** Liquibase
- **Build Tool:** Maven
- **Utilities:** Lombok
- **AI Integration:** Spring AI MCP

## 3. Commands

- **Build Project:**
  ```bash
  ./mvnw clean install
  ```
- **Run Application:**
  ```bash
  ./mvnw spring-boot:run
  ```
- **Run Tests:**
  ```bash
  ./mvnw test
  ```
- **Start Database (Docker):**
  ```bash
  docker-compose up -d
  ```

## 4. Project Structure & Architecture

The project follows a standard layered architecture pattern common in Spring Boot applications.

- **`controller`**: Contains REST controllers that handle incoming HTTP requests.
- **`service`**: Holds the core business logic.
    - **`service/api`**: Defines the service layer interfaces.
    - **`service/impl`**: Contains the implementations of the service interfaces.
- **`repository`**: Includes Spring Data JPA repositories for database interactions.
- **`model`**: Contains data-related classes.
    - **`model/entities`**: JPA entities that map to database tables.
    - **`model/dto`**: Data Transfer Objects used for API request/response payloads.
    - **`model/enums`**: Enumerations used throughout the application.
- **`resources/db/changelog`**: Stores database migration scripts managed by Liquibase.

## 5. Coding Conventions & Patterns

- **Interface-Based Services**: Always define business logic behind an interface in the `service.api` package and place
  the implementation in the `service.impl` package. This promotes loose coupling and easier testing.
- **DTOs for API Boundaries**: Controllers must use DTOs for request and response bodies. Do not expose JPA entities
  directly in the API layer to avoid unintended data exposure and to create stable API contracts.
- **Request Validation**: Use validation annotations (@Valid) on DTOs in controllers to enforce data integrity at the
  API boundary.
- **Use Lombok**: Utilize Lombok annotations (`@Data`, `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`,
  `@AllArgsConstructor`, etc.) to reduce boilerplate code in model, DTO, and entity classes.
- **Database Migrations**: All database schema changes (DDL) and reference data insertions (DML) must be performed
  through Liquibase scripts. Place new SQL scripts in the appropriate versioned folder under
  `src/main/resources/db/changelog/` and update the master changelog file.
- **Configuration**: All application configuration should be managed in `src/main/resources/application.yml`. Avoid
  hardcoding configuration values.
- **Testing**: Write unit and integration tests for new features. The project is configured to use JUnit 5 and an
  in-memory H2 database for the test scope.

### 5.1. Logging Conventions

- **Framework**: Use **SLF4J** with the `@Slf4j` annotation from Lombok for efficient and clean logger injection.
- **Log Levels**: Adhere to the following log level semantics:
    - **`ERROR`**: For critical failures and unhandled exceptions that stop a process. *Always* include the exception
      object to get a stack trace.
        - *Example*: Database connection failure, critical business rule violation.
    - **`WARN`**: For recoverable issues or potential problems that do not halt the current operation.
        - *Example*: Using a deprecated API, a missing optional configuration.
    - **`INFO`**: For significant, high-level business events and application lifecycle milestones.
        - *Example*: `Client created with ID: 12345`, `Application started successfully`.
    - **`DEBUG`**: For developer-focused information to diagnose issues, such as method entry/exit points and variable
      states.
        - *Example*: `Entering searchClients method with parameters: name=Corp`, `Found 5 clients`.
    - **`TRACE`**: For the most granular, step-by-step execution flow details.
- **Message Formatting**:
    - **Use Parameterized Logging**: Use `{}` placeholders for variables to improve performance and readability.
        - **Do**: `log.info("Client {} created", clientId);`
        - **Don't**: `log.info("Client " + clientId + " created");`
    - **Provide Context**: Include relevant identifiers (e.g., Client ID, Request ID) in messages to enable easy
      tracing.
- **What to Log**:
    - **Controllers**: Log request entry and successful completion at `DEBUG`.
    - **Services**: Log business logic execution, method flows at `DEBUG`, and key outcomes at `INFO`.
    - **Exception Handlers**: Log all caught exceptions at `ERROR` level, including the full stack trace. 
