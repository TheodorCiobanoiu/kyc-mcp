# REQ-007 - Integration Tests for MCP Tools

- **Author**: Theodor Ciobanoiu
- **Status**: To Do
- **Created**: 2025-07-20
- **Last Updated**: 2025-07-20

---

## 1. User Story

```
As a developer,
I want a comprehensive suite of automated integration tests,
So that I can verify that all MCP tools work correctly from end-to-end and prevent regressions.
```

### User Persona

- **Primary Persona**: Developer
- **Description**: A developer maintaining or extending the KYC MCP application who needs confidence that the core tool
  functionality is stable and correct.

## 2. Acceptance Criteria

### Scenario 1: Run All Tests Successfully

- **Given** The project code is checked out.
- **When** the command `./mvnw test` is executed.
- **Then** all integration tests for the MCP tools run and pass without any errors.

### Scenario 2: Test Data Isolation

- **Given** A test method creates new data (e.g., a new client).
- **When** the next test method runs.
- **Then** the data from the previous test does not interfere with the current test, thanks to transactional boundaries.

### Scenario 3: Test Coverage

- **Given** The integration test suite.
- **When** the tests are reviewed.
- **Then** it is confirmed that all 7 MCP tools have test coverage for both successful execution and expected
  error-handling scenarios.

## 3. Functional Requirements

### 3.1. Test Scope

The integration tests must cover the full flow for each MCP tool, from the tool invocation down to the database
interaction.

- **`create_client`**: Verify that a client is successfully created and the correct `McpServerResponse` is returned.
- **`get_client_details`**: Verify that after creating a client, it can be retrieved by its ID. Also, test for the case
  where a non-existent ID is requested, ensuring an error response is returned.
- **`add_person_to_client`**: Verify that a person can be successfully added to an existing client. Test the error case
  where the client ID does not exist.
- **`get_persons_by_client`**: Verify that persons added to a client are correctly retrieved.
- **`search_clients`**: Verify that the search functionality works correctly for various criteria (name, type, risk
  level).
- **`get_all_clients`**: Verify that the tool returns a list of all created clients.
- **`get_high_risk_clients`**: Verify that only clients with `RiskLevel.HIGH` are returned.
- **`get_beneficial_owners`**: Verify that only persons with `RelationshipType.BENEFICIAL_OWNER` are returned.

## 4. Non-Functional Requirements (NFRs)

- **Framework**: Tests will use JUnit 5 and the Spring Test framework (`@SpringBootTest`).
- **Database**: Tests must run against an in-memory H2 database, not the production PostgreSQL database. This is
  configured via a separate test profile.
- **Isolation**: Each test method must be isolated within a transaction that is rolled back at the end of its execution.
  This is achieved by annotating the test class or methods with `@Transactional`.
- **Execution**: Tests must be executable via the standard Maven build lifecycle (`./mvnw test`).

## 5. Technical Notes & Implementation Details

- **Affected Components**: `src/test/java/com/theociobanoiu/kycmcp/mcp/`
- **Configuration**: A test-specific configuration file, `src/test/resources/application-test.yml`, will be created to
  configure the H2 database connection for the `test` profile.
- **Key Logic**: A test class, `KycMcpToolsIntegrationTest`, will be created. It will be annotated with
  `@SpringBootTest` to load the full application context. The `KycMcpTools` bean will be injected into the test class to
  allow for direct invocation of the tools.

## 6. Assumptions & Dependencies

- **Dependencies**:
    - This requirement depends on the completion of all preceding requirements (REQ-000 through REQ-006) as it tests the
      implemented functionality.
    - The `h2database` dependency must be present in `pom.xml` with a `<scope>test</scope>`.

## 7. Implementation Plan

### Implementation Summary

Create a new integration test class, `KycMcpToolsIntegrationTest`, that loads the Spring context and tests each of the 7
MCP tools. The tests will run against an H2 in-memory database and ensure that each tool's success and failure paths
work as expected.

### Development Steps

1. **Create Test Configuration**:
    - *Details*: Create the `src/test/resources/application-test.yml` file. Configure the H2 database connection,
      specifying an in-memory mode (e.g., `jdbc:h2:mem:testdb`).
2. **Create Test Class**:
    - *Details*: Create the `KycMcpToolsIntegrationTest.java` file in the `src/test/java/com/theociobanoiu/kycmcp/mcp/`
      directory. Annotate it with `@SpringBootTest` and `@Transactional`.
3. **Inject Dependencies**:
    - *Details*: Inject the `KycMcpTools` bean into the test class.
4. **Write Test Methods**:
    - *Details*: Create individual `@Test` methods for each scenario described in section 3.1. Use assertions (e.g.,
      AssertJ's `assertThat`) to validate the contents of the `McpServerResponse` objects returned by the tools.
    - *Example Test Flow*: A test could first call `create_client`, assert the response is successful, then use the
      returned ID to call `get_client_details` and assert that the data matches.
5. **Verify Execution**:
    - *Details*: Run `./mvnw clean install` or `./mvnw test` to ensure all tests pass as part of the build.
