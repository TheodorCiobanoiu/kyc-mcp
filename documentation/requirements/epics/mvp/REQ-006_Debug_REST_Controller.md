# REQ-006 - Debug REST Controller for MCP Tool Testing

- **Author**: Theodor Ciobanoiu
- **Status**: To Do
- **Created**: 2025-07-20
- **Last Updated**: 2025-07-20

---

## 1. User Story

```
As a developer,
I want HTTP endpoints that mirror the MCP tools,
So that I can easily test the full functionality of each tool during development using standard tools like `curl` or a web browser.
```

### User Persona

- **Primary Persona**: Developer
- **Description**: A developer working on the KYC MCP application who needs a simple way to invoke and debug the MCP
  tools without needing a full AI client.

## 2. Acceptance Criteria

### Scenario 1: Successfully Call a Read-Only Endpoint

- **Given** The application is running.
- **When** a `GET` request is made to `/api/debug/clients`.
- **Then** the endpoint invokes the `get_all_clients` MCP tool.
- **And** an HTTP `200 OK` response is returned.
- **And** the response body contains the `McpServerResponse` object from the tool, serialized as JSON.

### Scenario 2: Successfully Call a Write-Operation Endpoint

- **Given** The application is running.
- **When** a `POST` request with a valid JSON body is made to `/api/debug/clients`.
- **Then** the endpoint invokes the `create_client` MCP tool.
- **And** an HTTP `200 OK` response is returned.
- **And** the response body contains the `McpServerResponse` from the successful creation.

### Scenario 3: Call an Endpoint that Results in a Tool Error

- **Given** The application is running.
- **When** a `GET` request is made to `/api/debug/clients/999`, where client 999 does not exist.
- **Then** the endpoint invokes the `get_client_details` tool, which returns an error.
- **And** an HTTP `200 OK` response is returned (as the controller-to-tool call was successful).
- **And** the response body contains the `McpServerResponse` object with a `status` of `ERROR` and the corresponding
  `McpServerError` details.

## 3. Functional Requirements

### 3.1. Controller Definition

- **Class**: `KycDebugController`
- **Package**: `com.theociobanoiu.kycmcp.controller`
- **Annotation**: `@RestController`
- **Base Path**: `/api/debug`

### 3.2. Endpoints

*Note: All endpoints will return a `ResponseEntity<McpServerResponse<?>>`.*

| Method | Path                    | MCP Tool Invoked        | Request Body/Params                               |
|:-------|:------------------------|:------------------------|:--------------------------------------------------|
| `GET`  | `/clients`              | `get_all_clients`       | None                                              |
| `GET`  | `/clients/high-risk`    | `get_high_risk_clients` | None                                              |
| `GET`  | `/clients/search`       | `search_clients`        | Request params: `name`, `clientType`, `riskLevel` |
| `GET`  | `/clients/{id}`         | `get_client_details`    | Path variable: `id`                               |
| `POST` | `/clients`              | `create_client`         | JSON body mapping to `create_client` tool params  |
| `POST` | `/clients/{id}/persons` | `add_person_to_client`  | Path variable: `id`, JSON body for person details |
| `GET`  | `/beneficial-owners`    | `get_beneficial_owners` | None                                              |

## 4. Non-Functional Requirements (NFRs)

- **Logging**: Log incoming requests at the `DEBUG` level. Log the name of the MCP tool being invoked at the `INFO`
  level.
- **Error Handling**: The controller should not contain business logic. It is a thin wrapper that directly calls the
  appropriate MCP tool and returns the `McpServerResponse` produced by it. It should handle potential exceptions during
  request processing and return appropriate HTTP status codes (e.g., `400 Bad Request` for invalid JSON).

## 5. Technical Notes & Implementation Details

- **Affected Components**: `controller` package.
- **Database Changes**: None.
- **Key Logic**: The `KycDebugController` will inject the `KycMcpTools` bean. Each endpoint method will call the
  corresponding method on the `KycMcpTools` instance and wrap the result in a `ResponseEntity.ok()` before returning.

## 6. Assumptions & Dependencies

- **Dependencies**:
    - This requirement depends on the completion of **REQ-004** and **REQ-005** where the MCP tools are defined.
    - It depends on **REQ-000** for the `McpServerResponse` structure.

## 7. Implementation Plan

### Implementation Summary

Create a new `@RestController` class named `KycDebugController`. This controller will have methods for each of the 7 MCP
tools. Each method will simply invoke the corresponding tool and return the tool's response within a `ResponseEntity`.

### Development Steps

1. **Create Controller Class**:
    - *Details*: Create `KycDebugController.java` in the `controller` package. Annotate it with `@RestController` and
      `@RequestMapping("/api/debug")`.
2. **Inject `KycMcpTools`**:
    - *Details*: Inject the `KycMcpTools` bean using constructor injection.
3. **Implement Endpoints**:
    - *Details*: Create a public method for each endpoint defined in the table in section 3.2. Use `@GetMapping`,
      `@PostMapping`, `@PathVariable`, and `@RequestParam` as appropriate to map the HTTP requests to the MCP tool
      method calls.
4. **Testing**:
    - *Integration Tests*: While full integration tests are covered in REQ-007, developers can use tools like `curl` or
      Postman to manually test the endpoints during development.
