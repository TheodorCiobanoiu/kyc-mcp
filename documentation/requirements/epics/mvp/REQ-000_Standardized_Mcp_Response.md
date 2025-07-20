# REQ-000 - Standardized MCP Server Response

- **Author**: Theodor Ciobanoiu
- **Status**: To Do
- **Created**: 2025-07-20
- **Last Updated**: 2025-07-20

---

## 1. User Story

```
As a developer,
I want a standardized, type-safe response wrapper for all MCP tools,
So that I can create robust and predictable tool interfaces for the AI agent.
```

## 2. Acceptance Criteria

### Scenario 1: Successful Response

- **Given** An MCP tool successfully retrieves or creates data (e.g., a `ClientDto`).
- **When** a successful response is created using `McpServerResponse.success(data)`.
- **Then** an `McpServerResponse` object is created with `status = SUCCESS`.
- **And** the `data` field contains the `ClientDto` payload.
- **And** the `error` field is `null`.

### Scenario 2: Error Response

- **Given** An MCP tool encounters an error (e.g., a resource is not found).
- **When** an error response is created using `McpServerResponse.error("NOT_FOUND", "Client not found")`.
- **Then** an `McpServerResponse` object is created with `status = ERROR`.
- **And** the `data` field is `null`.
- **And** the `error` field contains an `McpServerError` object with the specified code and message.

## 3. Functional Requirements

### 3.1. Data Models

- **`McpServerResponse<T>`**: A generic class to wrap all MCP tool responses.
    - `Status status`: An enum indicating the outcome (`SUCCESS` or `ERROR`).
    - `T data`: A generic field holding the successful response payload. It is `null` if `status` is `ERROR`.
    - `McpServerError error`: A record holding error details. It is `null` if `status` is `SUCCESS`.

- **`Status`**: An enum with two possible values:
    - `SUCCESS`
    - `ERROR`

- **`McpServerError`**: A record to provide structured error information.
    - `String code`: A machine-readable error code (e.g., `NOT_FOUND`, `INVALID_INPUT`).
    - `String message`: A human-readable description of the error.

### 3.2. Factory Methods

The `McpServerResponse<T>` class should provide static factory methods for easy instantiation:

- `public static <T> McpServerResponse<T> success(T data)`
- `public static <T> McpServerResponse<T> error(String code, String message)`

## 4. Non-Functional Requirements (NFRs)

- **Immutability**: The response object should be immutable to ensure thread safety.
- **Serialization**: The object must be serializable to JSON so the MCP framework can correctly process it.

## 5. Technical Notes & Implementation Details

- **Affected Components**: A new package `com.theociobanoiu.kycmcp.model.response` will be created to hold these
  classes.
- **Implementation**: `McpServerResponse` will be a standard Java class. `McpServerError` will be a Java `record`.
  `Status` will be an `enum`.

## 6. Assumptions & Dependencies

- **Dependencies**: This requirement will be a foundational dependency for all other requirements involving MCP tools (
  REQ-004, REQ-005, etc.).

## 7. Implementation Plan

### Implementation Summary

This plan details the creation of the `McpServerResponse<T>` class, its supporting `Status` enum, and `McpServerError`
record, leveraging Lombok for concise code.

### Development Steps

1. **Create the `response` Package**
    - **Action:** Create the directory structure for the new package.
    - **Command:**
      ```bash
      mkdir -p src/main/java/com/theociobanoiu/kycmcp/model/response
      ```

2. **Create `Status.java` Enum**
    - **Action:** Define the `Status` enum with `SUCCESS` and `ERROR` values.
    - **File to Create:** `src/main/java/com/theociobanoiu/kycmcp/model/response/Status.java`
    - **Code:**
      ```java
      package com.theociobanoiu.kycmcp.model.response;

      public enum Status {
          SUCCESS,
          ERROR
      }
      ```

3. **Create `McpServerError.java` Record**
    - **Action:** Define the `McpServerError` record with `code` and `message` fields, using Lombok's `@Builder` for
      convenient instantiation.
    - **File to Create:** `src/main/java/com/theociobanoiu/kycmcp/model/response/McpServerError.java`
    - **Code:**
      ```java
      package com.theociobanoiu.kycmcp.model.response;

      import lombok.Builder;

      @Builder
      public record McpServerError(String code, String message) {
      }
      ```

4. **Create `McpServerResponse.java` Class**
    - **Action:** Define the `McpServerResponse` class, including its fields, and use Lombok's
      `@AllArgsConstructor(access = AccessLevel.PRIVATE)` to generate the private constructor. Static factory methods
      for `success` and `error` responses will remain.
    - **File to Create:** `src/main/java/com/theociobanoiu/kycmcp/model/response/McpServerResponse.java`
    - **Code:**
      ```java
      package com.theociobanoiu.kycmcp.model.response;

      import lombok.AccessLevel;
      import lombok.AllArgsConstructor;
      import lombok.Getter;

      @Getter
      @AllArgsConstructor(access = AccessLevel.PRIVATE) // Generates a private constructor for all fields
      public class McpServerResponse<T> {
          private final Status status;
          private final T data;
          private final McpServerError error;

          public static <T> McpServerResponse<T> success(T data) {
              return new McpServerResponse<>(Status.SUCCESS, data, null);
          }

          public static <T> McpServerResponse<T> error(String code, String message) {
              return new McpServerResponse<>(Status.ERROR, null, McpServerError.builder().code(code).message(message).build());
          }
      }
      ```
