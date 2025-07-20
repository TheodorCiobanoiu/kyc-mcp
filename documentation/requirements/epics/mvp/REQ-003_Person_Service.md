# REQ-003 - Person Service for Client Relationship Management

- **Author**: Theodor Ciobanoiu
- **Status**: To Do
- **Created**: 2025-07-20
- **Last Updated**: 2025-07-20

---

## 1. User Story

```
As a developer,
I want a service layer that handles business logic for persons associated with clients,
So that I can manage relationships, such as adding beneficial owners, and retrieve person-specific data.
```

### User Persona

- **Primary Persona**: Developer
- **Description**: The developer implementing the MCP tools who needs a reliable service to manage individuals linked to
  a client entity.

## 2. Acceptance Criteria

### Scenario 1: Add a Person to an Existing Client

- **Given** A client with a specific ID exists.
- **And** Valid person data is provided in a `CreatePersonRequest` object.
- **When** the `addPersonToClient` method is called with the client ID and the request object.
- **Then** a new `Person` entity is created and associated with the correct client.
- **And** a `PersonDto` representing the new person is returned.

### Scenario 2: Attempt to Add a Person to a Non-Existent Client

- **Given** No client with a specific ID exists.
- **When** the `addPersonToClient` method is called with that ID.
- **Then** a `ResourceNotFoundException` (or a similar custom exception) is thrown.

### Scenario 3: Get All Persons for a Specific Client

- **Given** A client with associated persons exists.
- **When** `getPersonsByClient` is called with the client's ID.
- **Then** a list of `PersonDto` objects for that client is returned.

### Scenario 4: Get All Beneficial Owners

- **Given** Multiple persons with the relationship type `BENEFICIAL_OWNER` exist across different clients.
- **When** the `getBeneficialOwners` method is called.
- **Then** a list of all `PersonDto` objects with that specific relationship type is returned.

## 3. Functional Requirements

### 3.1. Service Interface (`PersonService.java`)

- **Package**: `com.theociobanoiu.kycmcp.service.api`
- **Methods**:
    - `PersonDto addPersonToClient(Long clientId, CreatePersonRequest request)`
    - `List<PersonDto> getPersonsByClient(Long clientId)`
    - `List<PersonDto> getBeneficialOwners()`

### 3.2. Service Implementation (`PersonServiceImpl.java`)

- **Package**: `com.theociobanoiu.kycmcp.service.impl`
- **Dependencies**: `PersonRepository`, `ClientRepository`.
- **Logic**:
    - **`addPersonToClient`**: First, it must verify the client exists by calling `clientRepository.findById()`. If not
      found, it throws an exception. If found, it maps the `CreatePersonRequest` to a `Person` entity, sets the client
      association, persists the new person, and returns the resulting `PersonDto`.
    - **`getPersonsByClient`**: Fetches all persons associated with the given `clientId`.
    - **`getBeneficialOwners`**: Fetches all persons where the `relationshipType` is `BENEFICIAL_OWNER`.

### 3.3. Data Models (DTOs)

- **Request DTOs**:
    - **`CreatePersonRequest`**: Contains fields to create a new person (e.g., `name`, `dateOfBirth`,
      `relationshipType`).
- **Response DTOs**: `PersonDto` (as defined in REQ-001).

## 4. Non-Functional Requirements (NFRs)

- **Transactional Integrity**: The `addPersonToClient` method must be annotated with `@Transactional`.
- **Logging**: Log method flows at `DEBUG` level. Log the addition of a new person at `INFO` level (e.g., "New person
  with ID {} added to client {}"). Log exceptions, especially for not-found clients, at `ERROR` level.
- **Error Handling**: The service must handle the case where a `clientId` does not exist by throwing a specific,
  catchable exception.

## 5. Technical Notes & Implementation Details

- **Affected Components**: `service/api`, `service/impl` packages.
- **Database Changes**: None.
- **Key Logic**: The service will validate the existence of the parent `Client` entity before creating a new `Person` to
  maintain data integrity.

## 6. Assumptions & Dependencies

- **Dependencies**:
    - Depends on **REQ-001** for `PersonDto` and **REQ-002** for `ClientService` (and its repository).
    - Depends on `PersonRepository` and `ClientRepository` interfaces.

## 7. Implementation Plan

### Implementation Summary

Define the `PersonService` interface and its implementation. The implementation will handle the business logic for
associating persons with clients, ensuring that a client exists before any new person is added to it.

### Development Steps

1. **Repository Layer (`PersonRepository`)**:
    - *Details*: To support the service logic, the `PersonRepository` interface must be updated.
    - Add the method `List<Person> findByClientId(Long clientId);` to retrieve all persons for a client.
    - Add the method `List<Person> findByRelationshipType(RelationshipType relationshipType);` to find all persons with
      a specific role, like beneficial owners.

2. **Define Interface (`PersonService`)**:
    - *Details*: Create the `PersonService.java` interface in `src/main/java/com/theociobanoiu/kycmcp/service/api/` with
      the three methods defined in section 3.1.

3. **Create Implementation Class (`PersonServiceImpl`)**:
    - *Details*: Create `PersonServiceImpl.java` in the `impl` package. It should implement `PersonService` and be
      annotated with `@Service`.

4. **Implement Methods**:
    - *Details*: Implement the service methods, injecting `PersonRepository` and `ClientRepository`. Ensure the
      `addPersonToClient` method includes the client existence check.

5. **Testing**:
    - *Unit Tests*: Write unit tests for `PersonServiceImpl`, mocking repository dependencies. Test the success path,
      and critically, the failure path where the client is not found.
