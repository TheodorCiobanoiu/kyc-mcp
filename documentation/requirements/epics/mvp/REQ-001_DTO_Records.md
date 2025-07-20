# REQ-001 - DTO Records for Clean Data Transfer

- **Author**: Theodor Ciobanoiu
- **Status**: To Do
- **Created**: 2025-07-20
- **Last Updated**: 2025-07-20

---

## 1. User Story

```
As a developer,
I want to use clean, immutable DTOs for data transfer between layers,
So that I can ensure data consistency, create stable API contracts, and improve thread safety.
```

### User Persona

- **Primary Persona**: Developer
- **Description**: The developer building the service and tool layers of the application who needs a reliable way to
  pass data without exposing internal entities.

## 2. Acceptance Criteria

A checklist of all conditions that must be met for this requirement to be considered complete.

### Scenario 1: Successful DTO Creation and Conversion

- **Given** A `Client` entity and a `Person` entity exist.
- **When** The `ClientDto.from()` or `PersonDto.from()` factory methods are called with an entity.
- **Then** A corresponding DTO record is created with all fields correctly mapped from the entity.

### Scenario 2: DTO Immutability

- **Given** A `ClientDto` or `PersonDto` instance is created.
- **When** An attempt is made to modify one of its fields.
- **Then** The compilation fails, as record fields are final by default, ensuring immutability.

### Scenario 3: Entity Conversion

- **Given** A `ClientDto` or `PersonDto` instance exists.
- **When** The `toEntity()` method is called.
- **Then** A new `Client` or `Person` entity is returned with its fields mapped from the DTO.

## 3. Functional Requirements

### 3.1. Data Models (DTOs)

- **Response DTOs**:
    - **`ClientDto`**: A record representing a client.
        - `UUID id`
        - `String name`
        - `ClientType clientType`
        - `RiskLevel riskLevel`
        - `List<PersonDto> persons` (Can be null or empty if not explicitly loaded)
    - **`PersonDto`**: A record representing an individual associated with a client.
        - `UUID id`
        - `String name`
        - `LocalDate dateOfBirth`
        - `RelationshipType relationshipType`
        - `RiskLevel riskLevel`

### 3.2. Factory and Conversion Methods

- **`ClientDto` Methods**:
    - `static ClientDto from(Client client)`: Creates a DTO from a single entity.
    - `static List<ClientDto> fromList(List<Client> clients)`: Creates a list of DTOs from a list of entities.
    - `ClientDto withPersons(List<PersonDto> persons)`: Creates a new `ClientDto` instance, copying existing data and
      adding the list of associated persons.
    - `Client toEntity()`: Converts the DTO back to a `Client` entity.
- **`PersonDto` Methods**:
    - `static PersonDto from(Person person)`: Creates a DTO from a single entity.
    - `static List<PersonDto> fromList(List<Person> persons)`: Creates a list of DTOs from a list of entities.
    - `Person toEntity()`: Converts the DTO back to a `Person` entity.

## 4. Non-Functional Requirements (NFRs)

- **Immutability**: DTOs must be immutable to ensure thread safety and predictable state. This will be achieved by using
  Java `record` types.
- **Performance**: DTO creation should be lightweight and have minimal performance overhead.
- **Validation**: The DTOs themselves do not perform validation; validation is handled at the API boundary (controllers)
  on request DTOs.

## 5. Technical Notes & Implementation Details

- **Affected Components**: `model/dto` package.
- **Database Changes**: None.
- **Key Logic**:
    - Implement `ClientDto` and `PersonDto` as Java `record` classes in the `com.theociobanoiu.kycmcp.model.dto`
      package.
    - The factory methods will handle the mapping logic from entity to DTO.
    - The `toEntity()` methods will handle the reverse mapping.

## 6. Assumptions & Dependencies

- **Dependencies**:
    - This requirement depends on the existence of the `Client` and `Person` JPA entities.
    - It also depends on the `ClientType`, `RiskLevel`, and `RelationshipType` enums being defined in the `model.enums`
      package.

## 7. Implementation Plan

### Implementation Summary

Create two Java record classes, `ClientDto` and `PersonDto`, in the specified DTO package. Implement static factory
methods for converting entities to DTOs and instance methods for converting DTOs back to entities.

### Development Steps

1. **Create Directory**:
    - *Details*: Ensure the directory `src/main/java/com/theociobanoiu/kycmcp/model/dto/` exists.
2. **Implement `PersonDto`**:
    - *Details*: Create the `PersonDto.java` record with the specified fields. Add the `from()`, `fromList()`, and
      `toEntity()` methods.
3. **Implement `ClientDto`**:
    - *Details*: Create the `ClientDto.java` record with the specified fields. Add the `from()`, `fromList()`,
      `withPersons()`, and `toEntity()` methods.
4. **Testing**:
    - *Unit Tests*: No dedicated tests are required for this step as per the development plan, but developers should
      compile the project to ensure the records and their methods are correctly defined.
