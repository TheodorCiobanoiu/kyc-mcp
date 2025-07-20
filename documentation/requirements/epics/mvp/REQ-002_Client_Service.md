# REQ-002 - Client Service for Business Logic

- **Author**: Theodor Ciobanoiu
- **Status**: To Do
- **Created**: 2025-07-20
- **Last Updated**: 2025-07-20

---

## 1. User Story

```
As a developer,
I want a service layer that handles all business logic related to clients,
So that I can separate concerns, ensure transactional integrity, and provide a clean API for the tool layer.
```

### User Persona

- **Primary Persona**: Developer
- **Description**: The developer implementing the MCP tools who needs a reliable and well-defined service to handle
  client data operations.

## 2. Acceptance Criteria

### Scenario 1: Search Clients

- **Given** Several clients exist in the database with different names, types, and risk levels.
- **When** the `searchClients` method is called with specific criteria (e.g., name "Corp", risk level "HIGH").
- **Then** a list of `ClientDto` objects matching the criteria is returned.
- **And** The search should be case-insensitive for names.

### Scenario 2: Get Client Details

- **Given** A client with a specific ID exists.
- **When** `getClientDetails` is called with that ID.
- **Then** an `Optional<ClientDto>` containing the client's details (including associated persons) is returned.

### Scenario 3: Get Non-Existent Client

- **Given** No client with a specific ID exists.
- **When** `getClientDetails` is called with that ID.
- **Then** an empty `Optional` is returned.

### Scenario 4: Create a New Client

- **Given** Valid client data is provided in a `CreateClientRequest` object.
- **When** the `createClient` method is called.
- **Then** a new client is persisted in the database.
- **And** a `ClientDto` representing the newly created client is returned.

## 3. Functional Requirements

### 3.1. Service Interface (`ClientService.java`)

- **Package**: `com.theociobanoiu.kycmcp.service.api`
- **Methods**:
    - `List<ClientDto> searchClients(String name, ClientType clientType, RiskLevel riskLevel)`
    - `Optional<ClientDto> getClientDetails(Long clientId)`
    - `List<ClientDto> getAllClients()`
    - `List<ClientDto> getHighRiskClients()`
    - `ClientDto createClient(CreateClientRequest request)`

### 3.2. Service Implementation (`ClientServiceImpl.java`)

- **Package**: `com.theociobanoiu.kycmcp.service.impl`
- **Dependencies**: `ClientRepository`, `PersonRepository`.
- **Logic**:
    - **`searchClients`**: Must build a dynamic query (e.g., using JPA Criteria API or Specification) to filter based on
      non-null parameters.
    - **`getClientDetails`**: Must fetch the client and its associated list of persons. It should use the
      `ClientDto.withPersons()` method to create the final DTO.
    - **`getAllClients`**: Fetches all clients without their associated persons for performance reasons.
    - **`getHighRiskClients`**: Fetches only clients where `riskLevel` is `HIGH`.
    - **`createClient`**: Maps the `CreateClientRequest` DTO to a `Client` entity, persists it, and returns the
      `ClientDto` of the saved entity.

### 3.3. Data Models (DTOs)

- **Request DTOs**:
    - **`CreateClientRequest`**: Contains fields necessary to create a new client (e.g., `name`, `clientType`, `email`,
      `riskLevel`).
- **Response DTOs**: `ClientDto`, `PersonDto` (as defined in REQ-001).

## 4. Non-Functional Requirements (NFRs)

- **Transactional Integrity**: All methods that modify data (like `createClient`) must be annotated with
  `@Transactional` to ensure atomicity.
- **Logging**: Log method entry and exit points at `DEBUG` level. Log significant events, like the creation of a new
  client, at `INFO` level (e.g., "New client created with ID: {}"). Log any exceptions at `ERROR` level.
- **Performance**: The `getAllClients` method should be optimized to avoid N+1 query problems. It should not fetch the
  `persons` collection for each client.
- **Validation**: The service layer assumes that input DTOs (`CreateClientRequest`) have been validated at the
  controller layer.

## 5. Technical Notes & Implementation Details

- **Affected Components**: `service/api`, `service/impl` packages.
- **Database Changes**: None.
- **Key Logic**: The implementation will rely heavily on Spring Data JPA repositories for data access and the DTO
  factory methods (from REQ-001) for data mapping.

## 6. Assumptions & Dependencies

- **Dependencies**:
    - This requirement depends on the completion of **REQ-001** for the DTOs.
    - It depends on the `ClientRepository` and `PersonRepository` interfaces being defined.

## 7. Implementation Plan

### Implementation Summary

Define the `ClientService` interface in the `api` package and its implementation in the `impl` package. The
implementation will use the injected `ClientRepository` to interact with the database and will use the DTOs defined in
the previous step for all data exchange.

### Development Steps

1. **Repository Layer (`ClientRepository`)**:
    - *Details*: To support the service logic, the `ClientRepository` interface must be updated.
    - Extend `JpaSpecificationExecutor<Client, Long>` to enable dynamic, criteria-based queries for the `searchClients`
      method.
    - Add the method `List<Client> findAllByRiskLevel(RiskLevel riskLevel);` to efficiently fetch high-risk clients.

2. **Define Interface (`ClientService`)**:
    - *Details*: Create the `ClientService.java` interface inside `src/main/java/com/theociobanoiu/kycmcp/service/api/`
      with the five methods defined in section 3.1.

3. **Create Implementation Class (`ClientServiceImpl`)**:
    - *Details*: Create `ClientServiceImpl.java` in the `impl` package. Annotate it with `@Service` and have it
      implement `ClientService`. Inject the `ClientRepository`.

4. **Implement Methods**:
    - *Details*: Implement each of the five service methods. The `searchClients` method will use a `Specification` to
      build and execute the query. The other methods will call the corresponding repository methods. Use
      `@Transactional` on `createClient`. Add logging as specified.

5. **Testing**:
    - *Unit Tests*: Create unit tests for the `ClientServiceImpl` class. Mock the `ClientRepository` dependency to test
      the service logic in isolation.
