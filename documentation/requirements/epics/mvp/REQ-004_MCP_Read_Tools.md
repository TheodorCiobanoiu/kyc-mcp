# REQ-004 - Read-Only MCP Tools for Client Data

- **Author**: Theodor Ciobanoiu
- **Status**: To Do
- **Created**: 2025-07-20
- **Last Updated**: 2025-07-20

---

## 1. User Story

```
As an AI agent,
I want to search and retrieve client information,
So that I can answer user queries about clients, their details, and their risk status.
```

### User Persona

- **Primary Persona**: AI Agent
- **Description**: An AI model integrated with the Spring AI MCP framework that uses tools to perform actions on behalf
  of a user.

## 2. Acceptance Criteria

### Scenario 1: Search for Clients

- **Given** The `ClientService` is available and there are clients in the database.
- **When** The `search_clients` MCP tool is called with parameters like `name="Global Corp"`.
- **Then** The tool returns a `Map` containing a list of `ClientDto` objects that match the search criteria.

### Scenario 2: Get Details for a Specific Client

- **Given** A client with ID `123` exists.
- **When** The `get_client_details` MCP tool is called with `clientId=123`.
- **Then** The tool returns a `Map` containing the detailed `ClientDto` for that client, including associated persons.

### Scenario 3: Tool Call with Invalid Input

- **Given** The `get_client_details` tool expects a numeric ID.
- **When** The tool is called with an invalid ID, such as `clientId="abc"`.
- **Then** The tool returns a `Map` containing an `error` key with a descriptive message about the invalid input.

### Scenario 4: Get All High-Risk Clients

- **Given** There are clients marked with `HIGH` risk level.
- **When** The `get_high_risk_clients` MCP tool is called.
- **Then** The tool returns a `Map` containing a list of `ClientDto` objects with `riskLevel = HIGH`.

## 3. Functional Requirements

### 3.1. MCP Tool Definitions

- **Location**: `com.theociobanoiu.kycmcp.mcp.KycMcpTools`

#### Tool 1: `search_clients`

- **Description**: Searches for clients based on optional filter criteria. All parameters are optional, but at least one
  should be provided for a meaningful search.
- **Parameters**:

  | Name         | Type         | Required | Description                                                  |
      | :----------- | :----------- | :------- | :----------------------------------------------------------- |
  | `name`       | `string`     | No       | The name of the client to search for (case-insensitive).     |
  | `clientType` | `ClientType` | No       | The type of the client (e.g., `INDIVIDUAL`, `CORPORATE`).    |
  | `riskLevel`  | `RiskLevel`  | No       | The risk level of the client (e.g., `LOW`, `MEDIUM`, `HIGH`).|
- **Returns**: An `McpServerResponse` containing a list of `ClientDto` objects under the `data` key.

#### Tool 2: `get_client_details`

- **Description**: Retrieves comprehensive details for a single client, including their associated persons.
- **Parameters**:
  | Name | Type | Required | Description |
  | :--------- | :----- | :------- | :---------------------------------------- |
  | `clientId` | `Long` | Yes | The unique identifier of the client. |
- **Returns**: An `McpServerResponse` containing the `ClientDto` under the `data` key, or an `McpServerError` if not
  found.

#### Tool 3: `get_all_clients`

- **Description**: Retrieves a list of all clients in the system. This view is simplified and does not include
  associated persons.
- **Parameters**: None.
- **Returns**: An `McpServerResponse` containing a list of `ClientDto` objects under the `data` key.

#### Tool 4: `get_high_risk_clients`

- **Description**: Retrieves a list of all clients that are classified with a high risk level.
- **Parameters**: None.
- **Returns**: An `McpServerResponse` containing a list of `ClientDto` objects under the `data` key.

## 4. Non-Functional Requirements (NFRs)

- **Logging**: Each tool execution must be logged at `INFO` level, including the tool name and parameters. Errors must
  be logged at `ERROR` level.
- **Error Handling**: Tools should gracefully handle exceptions from the service layer (e.g.,
  `ResourceNotFoundException`) and return a standardized error `Map` (e.g., `Map.of("error", "Client not found")`).
- **Registration**: All four methods must be annotated with `@McpFunction` to be automatically registered with the MCP
  server.

## 5. Technical Notes & Implementation Details

- **Affected Components**: `mcp/KycMcpTools.java`.
- **Database Changes**: None.
- **Key Logic**: A new class, `KycMcpTools`, will be created and annotated with `@Component`. It will inject the
  `ClientService`. Each public method in this class will correspond to an MCP tool and will call the appropriate method
  in the `ClientService`.

## 6. Assumptions & Dependencies

- **Dependencies**:
    - This requirement is critically dependent on the completion of **REQ-002** (`ClientService`).
    - The Spring AI MCP starter must be a dependency in the `pom.xml`.

## 7. Implementation Plan

### Implementation Summary

Create a new Spring `@Component` named `KycMcpTools`. This class will contain four public methods, each corresponding to
one of the read-only tools. These methods will be annotated with `@McpFunction` and will delegate the actual business
logic to the `ClientService`.

### Development Steps

1. **Create `KycMcpTools` Class**:
    - *Details*: Create the file `src/main/java/com/theociobanoiu/kycmcp/mcp/KycMcpTools.java`. Annotate the class with
      `@Component` and `@Slf4j`.
2. **Inject Service**:
    - *Details*: Inject the `ClientService` into the `KycMcpTools` class using constructor injection.
3. **Implement `search_clients` Tool**:
    - *Details*: Create the `search_clients` method with the specified parameters. Annotate it with `@McpFunction` and
      provide a clear description. The method body will call `clientService.searchClients(...)` and return the result in
      a `Map`.
4. **Implement Other Read Tools**:
    - *Details*: Implement `get_client_details`, `get_all_clients`, and `get_high_risk_clients` following the same
      pattern. Ensure proper error handling for `get_client_details` if the client is not found.
5. **Testing**:
    - *Integration Tests*: Integration tests are the primary way to validate these tools. Tests should be written (as
      per REQ-007) to call each tool and verify the correctness of the returned `Map`.
