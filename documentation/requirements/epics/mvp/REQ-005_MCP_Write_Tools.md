# REQ-005 - Write-Operation MCP Tools

- **Author**: Theodor Ciobanoiu
- **Status**: To Do
- **Created**: 2025-07-20
- **Last Updated**: 2025-07-20

---

## 1. User Story

```
As an AI agent,
I want to create new clients and add persons to them,
So that I can perform data modification tasks based on user instructions.
```

### User Persona

- **Primary Persona**: AI Agent
- **Description**: An AI model that needs to modify system data by creating new entities through tool calls.

## 2. Acceptance Criteria

### Scenario 1: Successfully Create a New Client

- **Given** Valid data for a new client is provided.
- **When** The `create_client` MCP tool is called with the data.
- **Then** A new client is created in the database.
- **And** The tool returns a `McpServerResponse` containing the `ClientDto` of the newly created client.

### Scenario 2: Successfully Add a Person to a Client

- **Given** An existing client with ID `123`.
- **And** Valid data for a new person is provided.
- **When** The `add_person_to_client` tool is called with the client ID and person data.
- **Then** A new person is associated with client `123` in the database.
- **And** The tool returns a `McpServerResponse` containing the `PersonDto` of the new person.

### Scenario 3: Attempt to Add Person to Non-Existent Client

- **Given** A client with ID `999` does not exist.
- **When** The `add_person_to_client` tool is called for client `999`.
- **Then** The tool returns a `McpServerResponse` with an `error` key indicating the client was not found.

### Scenario 4: Retrieve All Beneficial Owners

- **Given** The `PersonService` is available.
- **When** The `get_beneficial_owners` MCP tool is called.
- **Then** The tool returns a `McpServerResponse` containing a list of all `PersonDto`s who are beneficial owners.

## 3. Functional Requirements

### 3.1. MCP Tool Definitions

- **Location**: `com.theociobanoiu.kycmcp.mcp.KycMcpTools`

#### Tool 1: `create_client`

- **Description**: Creates a new client in the system.
- **Parameters**:

| Name         | Type         | Required | Description                                           |
|:-------------|:-------------|:---------|:------------------------------------------------------|
| `name`       | `string`     | Yes      | The full name of the client.                          |
| `clientType` | `ClientType` | Yes      | The type of client (e.g., `INDIVIDUAL`, `CORPORATE`). |
| `email`      | `string`     | No       | The client's email address.                           |
| `riskLevel`  | `RiskLevel`  | Yes      | The assigned risk level.                              |

- **Returns**: A `McpServerResponse` containing the created `ClientDto` under the `data` key.

#### Tool 2: `add_person_to_client`

- **Description**: Adds a new person (e.g., director, owner) and associates them with an existing client.
- **Parameters**:

| Name               | Type               | Required | Description                              |
|:-------------------|:-------------------|:---------|:-----------------------------------------|
| `clientId`         | `Long`             | Yes      | The ID of the client to associate with.  |
| `firstName`        | `string`           | Yes      | The person's first name.                 |
| `lastName`         | `string`           | Yes      | The person's last name.                  |
| `dateOfBirth`      | `string`           | Yes      | The person's date of birth (YYYY-MM-DD). |
| `relationshipType` | `RelationshipType` | Yes      | The person's relationship to the client. |

- **Returns**: A `McpServerResponse` containing the created `PersonDto` under the `data` key.

#### Tool 3: `get_beneficial_owners`

- **Description**: Retrieves a list of all persons identified as beneficial owners across all clients.
- **Parameters**: None.
- **Returns**: A `McpServerResponse` containing a list of `PersonDto` objects under the `data` key.

## 4. Non-Functional Requirements (NFRs)

- **Logging**: Log tool execution at `INFO` level. Log successful entity creation with the new ID. Log errors at `ERROR`
  level.
- **Error Handling**: Tools must catch exceptions from the service layer and return a standardized error
  `McpServerResponse`.
- **Validation**: The tool layer should perform basic parameter validation (e.g., checking for required fields) before
  calling the service.

## 5. Technical Notes & Implementation Details

- **Affected Components**: `mcp/KycMcpTools.java`.
- **Database Changes**: None directly, but these tools will add data to the `clients` and `persons` tables.
- **Key Logic**: These methods will be added to the existing `KycMcpTools` class. They will call the `ClientService` and
  `PersonService` to perform the write operations.

## 6. Assumptions & Dependencies

- **Dependencies**:
    - Depends on **REQ-002** (`ClientService`) and **REQ-003** (`PersonService`).
    - The corresponding `CreateClientRequest` and `CreatePersonRequest` DTOs must be available for mapping inside the
      tools.

## 7. Implementation Plan

### Implementation Summary

Add three new methods to the `KycMcpTools` class. Each method will be a tool responsible for a write operation (
`create_client`, `add_person_to_client`) or a specific read operation (`get_beneficial_owners`). They will delegate the
core logic to the appropriate service.

### Development Steps

1. **Inject `PersonService`**:
    - *Details*: Inject the `PersonService` into the `KycMcpTools` class alongside the `ClientService`.
2. **Implement `create_client` Tool**:
    - *Details*: Create the `create_client` method. It will construct a `CreateClientRequest` DTO from the tool
      parameters and pass it to `clientService.createClient()`.
3. **Implement `add_person_to_client` Tool**:
    - *Details*: Create the `add_person_to_client` method. It will construct a `CreatePersonRequest` and call
      `personService.addPersonToClient()`.
4. **Implement `get_beneficial_owners` Tool**:
    - *Details*: Create the `get_beneficial_owners` method that calls `personService.getBeneficialOwners()`.
5. **Testing**:
    - *Integration Tests*: Add integration tests (as per REQ-007) to verify that each tool correctly modifies the
      database and returns the expected result.
