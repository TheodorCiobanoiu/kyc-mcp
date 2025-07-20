# [REQ-ID] - [Requirement Title]

- **Epic**: [Link to Epic]
- **Author**: Theodor Ciobanoiu
- **Status**: To Do | In Progress | In Review | Done
- **Created**: YYYY-MM-DD
- **Last Updated**: YYYY-MM-DD

---

## Table of Contents

- [1. User Story](#1-user-story)
- [2. Acceptance Criteria](#2-acceptance-criteria)
- [3. Functional Requirements](#3-functional-requirements)
- [4. Non-Functional Requirements (NFRs)](#4-non-functional-requirements-nfrs)
- [5. Technical Notes & Implementation Details](#5-technical-notes--implementation-details)
- [6. Assumptions & Dependencies](#6-assumptions--dependencies)
- [7. Implementation Plan](#7-implementation-plan)

---

## 1. User Story

```
As a [User Persona],
I want to [Perform an Action],
So that I can [Achieve a Benefit].
```

### User Persona

- **Primary Persona**: [e.g., AI Agent, Compliance Officer]
- **Description**: [Brief description of the user and their primary goal]

## 2. Acceptance Criteria

A checklist of all conditions that must be met for this requirement to be considered complete. The Gherkin (
Given/When/Then) format is preferred for clarity and testability.

### Scenario 1: [Descriptive Name of the Scenario]

- **Given** [A specific precondition or context]
- **When** [A specific action is performed by the user]
- **Then** [A specific, verifiable outcome should occur]

### Scenario 2: [Error Handling or Edge Case]

- **Given** [A specific edge case context]
- **When** [An action that triggers an error is performed]
- **Then** [The system should respond with a a specific error message or state]

## 3. Functional Requirements

### 3.1. MCP Tool Definition

- **Tool Name**: `tool_name`
- **Description**: [A clear, one-sentence description of what the tool does.]

#### Parameters

| Name      | Type         | Required | Description                            |
|:----------|:-------------|:---------|:---------------------------------------|
| `param_1` | `string`     | Yes      | [Description of the first parameter.]  |
| `param_2` | `ClientType` | No       | [Description of the second parameter.] |

#### Return Value

- **On Success**: [Description of the successful response. e.g.,
  `Returns a ClientDTO object with the client's details.`]
- **On Failure**: [Description of the error response. e.g.,
  `Returns a standardized error object with a message and error code.`]

### 3.2. Data Models (DTOs)

- **Request DTOs**: [e.g., `CreateClientRequest` - include fields and validations]
- **Response DTOs**: [e.g., `ClientDTO` - include fields]

## 4. Non-Functional Requirements (NFRs)

- **Performance**: [e.g., The API response time for this tool must be under 500ms for the 95th percentile.]
- **Security
  **: [e.g., All input parameters must be sanitized to prevent injection attacks. The tool requires authentication.]
- **Logging**: [e.g., Log successful tool execution at INFO level, including the
  `clientId`. Log any errors at ERROR level with the full exception.]
- **Validation**: [e.g., `clientId` must be a valid UUID format. `riskLevel` must be one of the predefined enum values.]

## 5. Technical Notes & Implementation Details

- **Affected Services**: [e.g., `ClientService`, `PersonRepository`]
- **Database Changes**: [e.g., None, or "A new index will be added to the `clients` table on the `risk_level` column."]
- **Key Logic
  **: [Brief outline of the implementation logic. e.g., "The service will first validate the input. It will then call the repository to fetch the data and map the entity to the DTO before returning."]

## 6. Assumptions & Dependencies

- **Assumptions**: [e.g., "It is assumed that the upstream service providing client data is available."]
- **Dependencies**: [e.g., "This story depends on the completion of REQ-MVP-001 for the `ClientDTO` to be available."]

## 7. Implementation Plan

*This section should be filled out by the development team before starting work. If a step is not applicable, it should
be removed from the final document.*

### Implementation Summary

[Provide a high-level summary of the development approach. For example: "This will be implemented by adding a new method to the
`ClientService` and a corresponding query in the `ClientRepository`. A new MCP tool will be created in the
`KycMcpTools` class to expose this functionality. Unit and integration tests will be added to ensure correctness."]

### Development Steps

1. **Data Model (DTOs/Entities)**
    -
    *Details*: [Describe the specific DTOs or entities to be created or modified. Include field names, types, and any validation annotations.]

2. **Repository Layer**
    -
    *Details*: [Describe the new methods to be added to the JPA repository interfaces. Specify the query if it's complex.]

3. **Service Layer**
    -
    *Details*: [Describe the new methods to be added to the service interfaces and implementation classes. Outline the core business logic, including validation, data retrieval, and mapping.]

4. **Tool Layer (MCP)**
    -
    *Details*: [Describe the new MCP tool to be created. Specify the tool name, parameters, and how it maps to the service layer.]

5. **Testing**
    - *Unit
      Tests*: [Describe the specific unit tests to be written for the service layer. Focus on testing the business logic.]
    - *Integration
      Tests*: [Describe the integration tests for the MCP tool. This should cover the full flow from the tool definition to the database.]

6. **Documentation**
    - *Details*: [Specify any documentation that needs to be updated, such as the
      `GEMINI.md` file or other relevant documents.]
