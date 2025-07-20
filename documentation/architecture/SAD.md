# Software Architecture Document (SAD)

**Project:** Know Your Customer - Model Context Protocol (KYC-MCP)
**Version:** 1.0
**Date:** 2025-07-19
**Author:** Theodor Ciobanoiu

---

## Table of Contents

- [1. Introduction](#1-introduction)
    - [1.1. Purpose](#11-purpose)
    - [1.2. Scope](#12-scope)
    - [1.3. Definitions, Acronyms, and Abbreviations](#13-definitions-acronyms-and-abbreviations)
    - [1.4. References](#14-references)
- [2. Architectural Goals and Constraints](#2-architectural-goals-and-constraints)
    - [2.1. Architectural Goals](#21-architectural-goals)
    - [2.2. Architectural Constraints](#22-architectural-constraints)
- [3. Architectural Views](#3-architectural-views)
    - [3.1. Logical View](#31-logical-view)
    - [3.2. Development View](#32-development-view)
    - [3.3. Process View](#33-process-view)
    - [3.4. Deployment View](#34-deployment-view)
- [4. Data Architecture](#4-data-architecture)
    - [4.1. Data Model](#41-data-model)
    - [4.2. Data Persistence](#42-data-persistence)
    - [4.3. Database Migrations](#43-database-migrations)

---

## 1. Introduction

### 1.1. Purpose

This document provides a comprehensive architectural overview of the KYC-MCP application. It details the key
architectural views, design decisions, and technical constraints to guide the development team, stakeholders, and future
architects.

### 1.2. Scope

The scope of this document is the architecture of the KYC-MCP backend service. This includes its internal components,
its interaction with the database, and how it exposes its functionality to external clients via the Model Context
Protocol (MCP).

### 1.3. Definitions, Acronyms, and Abbreviations

| Term    | Definition                                                                     |
|:--------|:-------------------------------------------------------------------------------|
| **KYC** | Know Your Customer                                                             |
| **MCP** | Model Context Protocol: An open-source protocol for AI model-tool interaction. |
| **PRD** | Product Requirements Document                                                  |
| **SAD** | Software Architecture Document                                                 |
| **DTO** | Data Transfer Object                                                           |
| **JPA** | Jakarta Persistence API                                                        |
| **API** | Application Programming Interface                                              |

### 1.4. References

- [Product Requirements Document (PRD)](../requirements/PRD.md)
- [Project Configuration (GEMINI.md)](../../GEMINI.md)

---

## 2. Architectural Goals and Constraints

### 2.1. Architectural Goals

The architecture is designed to achieve the following goals:

- **Modularity:** Components are designed to be independent and loosely coupled to promote maintainability and parallel
  development.
- **Testability:** The separation of concerns allows for focused unit and integration testing, ensuring high code
  quality.
- **Scalability:** The stateless nature of the service and use of a robust database allows for horizontal scaling.
- **Clarity:** The architecture follows well-known design patterns to be easily understood by new developers.

### 2.2. Architectural Constraints

The project operates under the following constraints:

- **Technology Stack:** The system must be built using the Java/Spring Boot ecosystem as defined in the project
  configuration.
- **Database:** The primary data store must be a PostgreSQL database.
- **Protocol:** The application must expose its functionalities as tools conforming to the Model Context Protocol (MCP).
- **Schema Management:** All database schema changes must be managed via Liquibase.

---

## 3. Architectural Views

This section describes the system from different perspectives, following a simplified 4+1 view model.

### 3.1. Logical View

The logical view describes the system's functionality and the key components that provide it. The system is organized
into a classic three-layer architecture.

- **Tool Layer (Presentation):** This layer is responsible for exposing the application's functionality to the outside
  world. It consists of MCP Tools (`KycMcpTools`) that handle incoming requests from an MCP client, deserialize
  parameters, and delegate calls to the service layer.
- **Service Layer (Business Logic):** This is the core of the application. It contains the business logic, orchestrates
  data access, and ensures data integrity. It is defined by interfaces (`ClientService`, `PersonService`) with
  implementations (`ClientServiceImpl`, `PersonServiceImpl`).
- **Data Access Layer (Persistence):** This layer is responsible for all interactions with the database. It uses Spring
  Data JPA repositories (`ClientRepository`, `PersonRepository`) to perform CRUD operations on the data entities.

### 3.2. Development View

The development view outlines the organization of the source code.

The project follows the standard Maven directory structure. The core application code is located in
`src/main/java/com/theociobanoiu/kycmcp/` and is organized into the following packages:

- `controller`: Contains MCP tool definitions.
- `service/api`: Contains the service layer interfaces.
- `service/impl`: Contains the service layer implementations.
- `repository`: Contains the Spring Data JPA repository interfaces.
- `model/dto`: Contains Data Transfer Objects for API communication.
- `model/entities`: Contains JPA entities that map to database tables.
- `model/enums`: Contains application-wide enumerations.
- `resources/db/changelog`: Contains Liquibase scripts for database migrations.

### 3.3. Process View

The process view describes the dynamic aspects of the system.

The application runs as a single process within a Java Virtual Machine (JVM). When a request is made by an MCP client,
the following sequence occurs:

1. The Spring Boot web server receives the request.
2. The request is routed to the appropriate MCP tool in the `KycMcpTools` class.
3. The tool method calls the relevant service layer method (e.g., `ClientService.createClient`).
4. The service method executes its business logic, potentially calling the repository layer.
5. The repository layer interacts with the PostgreSQL database via JDBC.
6. The response flows back through the layers and is sent to the MCP client.

### 3.4. Deployment View

The deployment view describes the physical deployment of the system.

The system consists of two main components:

1. **KYC-MCP Application:** A self-contained Spring Boot application, packaged as a runnable JAR file. It can be run on
   any machine with a compatible JVM.
2. **PostgreSQL Database:** A relational database that is managed by Docker and runs in its own container, as defined in
   the `docker-compose.yml` file.

The application connects to the database via a JDBC connection string defined in the `application.yml` file.

---

## 4. Data Architecture

### 4.1. Data Model

The primary data entities are `Client` and `Person`. These are JPA entities that are mapped to tables in the PostgreSQL
database. See the `model/entities` package for details.

### 4.2. Data Persistence

Persistence is managed by Spring Data JPA and Hibernate. The service layer interacts with repository interfaces, which
Spring automatically implements at runtime.

### 4.3. Database Migrations

Database schema and reference data are managed exclusively through Liquibase scripts located in
`src/main/resources/db/changelog`. This ensures that database changes are version-controlled and repeatable.
