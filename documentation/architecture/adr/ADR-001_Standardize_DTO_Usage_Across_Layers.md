# ADR-001: Standardize DTO Usage Across Layers

- **Status**: Accepted
- **Date**: 2025-07-19
- **Authors**: Theodor Ciobanoiu
- **Deciders**: Theodor Ciobanoiu

---

## Table of Contents

- [1. Context](#1-context)
- [2. Decision](#2-decision)
- [3. Consequences](#3-consequences)
    - [3.1. Positive Consequences](#31-positive-consequences)
    - [3.2. Negative Consequences](#32-negative-consequences)
    - [3.3. Neutral Consequences](#33-neutral-consequences)
- [4. Alternatives Considered](#4-alternatives-considered)
- [5. References](#5-references)
- [6. Enforcement](#6-enforcement)

---

## 1. Context

The KYC-MCP application is designed with a layered architecture (Tool/Presentation, Service, Persistence). Data is
exchanged between these layers, and also with external MCP clients. Currently, there is a potential for JPA entities (
`Client`, `Person`) to be directly exposed in the Tool/Presentation layer (e.g., as return types from MCP tools or as
request bodies). This practice can lead to several issues:

- **Tight Coupling:** Direct exposure of entities couples the API contract to the internal database schema, making
  schema changes difficult without breaking external consumers.
- **Security Risks:** Sensitive internal fields or relationships might be inadvertently exposed to external clients.
- **Over-fetching/Under-fetching:** Entities often contain more data than required by a specific API endpoint, leading
  to unnecessary data transfer or requiring complex `@JsonIgnore` annotations.
- **Validation Complexity:** Applying validation rules directly to entities can be cumbersome and might not align with
  specific API input requirements.

This decision aims to establish a clear standard for data transfer between layers, particularly at the API boundary, to
ensure maintainability, security, and flexibility.

*Relevant documents: [SAD.md](../SAD.md), [PRD.md](../../requirements/PRD.md)*

## 2. Decision

We will standardize the use of Data Transfer Objects (DTOs) for all data exchange at the boundaries of the
Tool/Presentation layer (i.e., for all MCP tool request parameters and return types). JPA entities will remain strictly
within the Service and Persistence layers.

- **Request DTOs:** All incoming data for MCP tool parameters will be mapped to dedicated Request DTOs. These DTOs will
  contain only the necessary fields for the operation and will be the primary place for input validation (e.g., using
  `@Valid` annotations).
- **Response DTOs:** All data returned by MCP tools will be mapped from entities (or other internal data structures) to
  dedicated Response DTOs. These DTOs will contain only the data relevant for the external consumer.
- **Internal Mapping:** Mapping between DTOs and entities will occur within the Service layer, or explicitly within the
  Tool layer before calling the Service layer, using mapping libraries (e.g., MapStruct, ModelMapper) or manual mappers.

## 3. Consequences

### 3.1. Positive Consequences

- **Decoupling:** The API contract becomes independent of the internal database schema, allowing for easier evolution of
  both without breaking external consumers.
- **Enhanced Security:** Sensitive internal fields in entities are never directly exposed to the API, reducing data
  leakage risks.

- **Improved Performance:** DTOs can be tailored to fetch and return only the required data, optimizing payload sizes.
- **Clearer Validation:** Validation logic is centralized and explicit on DTOs, making it easier to manage and test.
- **Better Maintainability:** Changes to the database schema or internal logic are less likely to impact the external
  API.

### 3.2. Negative Consequences

- **Increased Boilerplate:** Requires creating additional DTO classes for each API request/response and implementing
  mapping logic.
- **Initial Development Overhead:** Slightly more time is needed during initial development to define DTOs and mappers.

### 3.3. Neutral Consequences

- Requires a mapping strategy (manual mappers or a mapping library).

## 4. Alternatives Considered

- **Direct Entity Exposure:** Expose JPA entities directly at the API layer.
    - Pros: Less boilerplate code, faster initial development.
    - Cons: Tight coupling, security risks, potential for over-fetching, complex validation.

- **Partial Entity Exposure (e.g., using `@JsonView` or `@JsonIgnore`):** Use annotations on entities to control
  serialization.
    - Pros: Avoids creating separate DTO classes.
    - Cons: Can become complex and difficult to manage for multiple API versions or varied data requirements; still
      couples API to entity structure; less explicit.

## 5. References

- [Spring Boot DTO Best Practices](https://www.baeldung.com/spring-boot-dto)
- [Model Context Protocol Specification](https://modelcontextprotocol.io/)

## 6. Enforcement

This decision will be enforced primarily through automated checks and code review processes.

- **Automated Checks**: An ArchUnit rule will be implemented to prevent direct usage of JPA entities (classes in
  `com.theociobanoiu.kycmcp.model.entities`) within the `com.theociobanoiu.kycmcp.controller` package. This rule will be
  integrated into the project's build pipeline (e.g., Maven `test` phase) and will fail the build if violated.
- **Code Review**: All pull requests will be subject to code review, where adherence to DTO usage standards will be
  explicitly checked.
