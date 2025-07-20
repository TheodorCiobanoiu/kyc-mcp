# Gemini Project Configuration: kyc-mcp

This document provides project-specific context to the Gemini AI assistant. Adhering to these guidelines will ensure
consistency and leverage the existing patterns within the project.

---

## Table of Contents

- [1. Project Overview](#1-project-overview)
- [2. Key Technologies](#2-key-technologies)
- [3. Commands](#3-commands)
- [4. Project Structure & Architecture](#4-project-structure--architecture)
- [5. Coding Conventions & Patterns](#5-coding-conventions--patterns)
    - [5.1. Logging Conventions](#51-logging-conventions)
- [6. Documentation](#6-documentation)
    - [6.1. Repository Documentation](#61-repository-documentation)
    - [6.2. Javadoc Conventions](#62-javadoc-conventions)
- [7. Gemini Operational Guidelines](#7-gemini-operational-guidelines)

---

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

## 6. Documentation

### 6.1. Repository Documentation

- **Location**: All project documentation, such as Product Requirements Documents (PRDs), Software Architecture
  Documents (SADs), and Architecture Decision Records (ADRs), is located in the `/documentation` directory.
- **Structure**: The `/documentation` directory is organized into subfolders like `requirements`, `architecture`, and
  `epics`, reflecting different aspects of the project.
- **Purpose**: These documents provide high-level overviews, detailed requirements, design decisions, and historical
  context for the project.
- **Maintenance**: This documentation must be kept up to date. Any changes to product requirements, architecture, or key
  design decisions should be reflected in the relevant documents.
- **AI Interaction**: As an AI agent, you need to always analyze these documents to understand project context,
  conventions, and requirements. You will use them to guide my actions, ensure adherence to project standards, and
  propose relevant changes or additions.

### 6.2. Javadoc Conventions

- **Purpose**: Javadoc comments are essential for documenting Java code directly within the source files. They provide
  structured information that is valuable for both human developers and AI agents (like yourself) in understanding the
  codebase.
- **Placement**: Javadoc should primarily be written at the **class/enum/record level**. This provides a high-level
  overview of the component's purpose and overall design. If a given method / field etc... that can be traditionally
  described as an complex business logic or it cannot be properly understood just by analyzing the code by a human
  being, then it should also have JavaDocs that properly explain the business logic or its general purpose.
- **Content Focus**:
    - **Class/Enum/Record Level**: Focus on *what* the component is, *why* it exists, and its overall responsibility
      within the system. For generic classes, use `@param <T>` to describe type parameters.
    - **Method/Field Level**: Javadoc for individual methods or fields should be used **sparingly**. Only add
      method/field Javadoc when:
        - The method's purpose is not immediately clear from its name or signature.
        - It involves complex logic or side effects that are not obvious.
        - It has specific pre-conditions, post-conditions, or throws exceptions that need explicit documentation.
        - It's part of a public API that requires formal documentation for consumers.
        - If documenting a method inside a service class, add the documentation in the interface for the service, not in
          the implementation
- **AI Assistance**: Adhering to these conventions helps you, the Gemini AI agent, better understand the code's intent,
  enabling me to generate more accurate code, suggest relevant refactorings, and answer questions about the codebase
  more effectively. Clear Javadoc reduces ambiguity and improves my ability to assist you.

## 7. Gemini Operational Guidelines

- **File Staging**: Always stage new files when created.
- **Requirement Progression**: When working on a requirement, never move to a different one without explicit approval
  from the user.
