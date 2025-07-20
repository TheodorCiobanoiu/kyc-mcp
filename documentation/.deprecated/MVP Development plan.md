# Sprint 1 MVP - Step-by-Step Implementation Guide

## Goal

Build a working MCP server with 7 core KYC tools that can be called by any MCP client.

---

## Step 1: Create DTO Records

**Story**: As a developer, I want clean, immutable DTOs for data transfer between layers.

**Tasks**:

- [ ] Create `src/main/java/com/theociobanoiu/kycmcp/model/dto/` directory
- [ ] Implement `ClientDto` record with factory methods (`from()`, `fromList()`, `withPersons()`)
- [ ] Implement `PersonDto` record with factory methods (`from()`, `fromList()`)
- [ ] Add `toEntity()` methods for converting back to entities
- [ ] Test compilation and basic factory methods

**Acceptance Criteria**:

- Records compile without errors
- Factory methods create DTOs from entities correctly
- DTOs are immutable and thread-safe

---

## Step 2: Implement ClientService

**Story**: As a developer, I want a service layer that handles client business logic and uses DTOs.

**Tasks**:

- [ ] Create `src/main/java/com/theociobanoiu/kycmcp/service/` directory
- [ ] Implement `ClientService` with methods:
    - `searchClients(name, clientType, riskLevel)` → `List<ClientDto>`
    - `getClientDetails(clientId)` → `Optional<ClientDto>`
    - `getAllClients()` → `List<ClientDto>`
    - `getHighRiskClients()` → `List<ClientDto>`
    - `createClient(...)` → `ClientDto`
- [ ] Use repository methods and DTO factory methods
- [ ] Add proper logging and transaction annotations

**Acceptance Criteria**:

- All service methods work with test data
- DTOs are properly converted from entities
- Transactions work correctly

---

## Step 3: Implement PersonService

**Story**: As a developer, I want a service layer that handles person business logic.

**Tasks**:

- [ ] Implement `PersonService` with methods:
    - `addPersonToClient(clientId, ...)` → `PersonDto`
    - `getPersonsByClient(clientId)` → `List<PersonDto>`
    - `getBeneficialOwners()` → `List<PersonDto>`
- [ ] Handle client validation and entity relationships
- [ ] Use DTO factory methods for responses

**Acceptance Criteria**:

- Can add persons to existing clients
- Properly validates client exists before adding person
- Returns correctly formatted PersonDto responses

---

## Step 4: Create Basic MCP Tools (Part 1 - Read Operations)

**Story**: As an AI agent, I want to search and retrieve client information via MCP tools.

**Tasks**:

- [ ] Create `src/main/java/com/theociobanoiu/kycmcp/mcp/` directory
- [ ] Implement `KycMcpTools` class with first 4 MCP functions:
    - `@McpFunction search_clients` - calls ClientService.searchClients()
    - `@McpFunction get_client_details` - calls ClientService.getClientDetails()
    - `@McpFunction get_all_clients` - calls ClientService.getAllClients()
    - `@McpFunction get_high_risk_clients` - calls ClientService.getHighRiskClients()
- [ ] Return standardized `Map<String, Object>` responses with success/error handling
- [ ] Add proper logging for each MCP call

**Acceptance Criteria**:

- All 4 MCP functions are registered and callable
- Functions return proper JSON-like Map responses
- Error handling works for invalid inputs

---

## Step 5: Create MCP Tools (Part 2 - Write Operations)

**Story**: As an AI agent, I want to create clients and add persons via MCP tools.

**Tasks**:

- [ ] Add remaining 3 MCP functions to `KycMcpTools`:
    - `@McpFunction create_client` - calls ClientService.createClient()
    - `@McpFunction add_person_to_client` - calls PersonService.addPersonToClient()
    - `@McpFunction get_beneficial_owners` - calls PersonService.getBeneficialOwners()
- [ ] Handle parameter validation and type conversion
- [ ] Ensure all responses follow the same format

**Acceptance Criteria**:

- Can create new clients via MCP
- Can add persons to clients via MCP
- All 7 MCP tools are working and registered

---

## Step 6: Create Debug REST Controller

**Story**: As a developer, I want HTTP endpoints to test MCP functionality during development.

**Tasks**:

- [ ] Create `src/main/java/com/theociobanoiu/kycmcp/controller/` directory
- [ ] Implement `KycDebugController` with REST endpoints that mirror MCP tools:
    - `GET /api/debug/clients` - calls get_all_clients MCP tool
    - `GET /api/debug/clients/high-risk` - calls get_high_risk_clients MCP tool
    - `GET /api/debug/clients/search?name=...` - calls search_clients MCP tool
    - `GET /api/debug/clients/{id}` - calls get_client_details MCP tool
    - `POST /api/debug/clients` - calls create_client MCP tool
    - `GET /api/debug/beneficial-owners` - calls get_beneficial_owners MCP tool
- [ ] Test all endpoints return proper JSON responses

**Acceptance Criteria**:

- All REST endpoints work and return JSON
- Can test all MCP functionality via HTTP calls
- Responses match MCP tool output format

---

## Step 7: Create Integration Tests

**Story**: As a developer, I want automated tests to verify MCP tools work correctly.

**Tasks**:

- [ ] Create `src/test/resources/application-test.yml` with H2 database config
- [ ] Create `src/test/java/com/theociobanoiu/kycmcp/mcp/` directory
- [ ] Implement `KycMcpToolsIntegrationTest` with tests for:
    - All 7 MCP tools work with sample data
    - Error handling for invalid inputs
    - Data persistence across multiple calls
- [ ] Use `@SpringBootTest` and `@Transactional` for proper test isolation
- [ ] Verify tests run with `./mvnw test`

**Acceptance Criteria**:

- All tests pass consistently
- Tests cover both success and error scenarios
- Test database is properly isolated

---

## Step 8: Verify MCP Server Integration

**Story**: As a developer, I want to confirm the MCP server is properly configured and tools are registered.

**Tasks**:

- [ ] Start application with `./mvnw spring-boot:run`
- [ ] Verify MCP endpoints are available:
    - `GET http://localhost:8080/mcp` - shows server info
    - `GET http://localhost:8080/mcp/tools` - lists available tools
- [ ] Test MCP tool calls via HTTP:
    - `POST http://localhost:8080/mcp/tools/get_all_clients`
    - `POST http://localhost:8080/mcp/tools/search_clients`
- [ ] Verify all 7 tools are listed and callable

**Acceptance Criteria**:

- MCP server starts without errors
- All 7 tools are registered and visible
- Can call MCP tools via HTTP POST requests
- Responses are properly formatted JSON

---

## Step 9: Create Demo Documentation

**Story**: As a developer, I want documentation showing how to demo the MVP functionality.

**Tasks**:

- [ ] Create demo script with curl commands for all MCP tools
- [ ] Document setup steps (database, application startup)
- [ ] Create example requests/responses for each tool
- [ ] Add troubleshooting section for common issues

**Acceptance Criteria**:

- Demo script works end-to-end
- Documentation is clear and complete
- Examples show real data from sample database

---

## Step 10: Sprint 1 Demo & Retrospective

**Story**: As a team, we want to demo the MVP and plan the next sprint.

**Tasks**:

- [ ] Demo all 7 MCP tools working with Claude or MCP client
- [ ] Show error handling and data persistence
- [ ] Document what worked well and what could be improved
- [ ] Plan Sprint 2 features based on MVP feedback

**Acceptance Criteria**:

- MVP successfully demonstrates MCP integration
- All success criteria from original plan are met
- Team is ready to start Sprint 2 planning

---

## Quick Reference - File Checklist

```
src/main/java/com/theociobanoiu/kycmcp/
├── model/dto/
│   ├── ClientDto.java          ✅ Step 1
│   └── PersonDto.java          ✅ Step 1
├── service/
│   ├── ClientService.java      ✅ Step 2
│   └── PersonService.java      ✅ Step 3
├── mcp/
│   └── KycMcpTools.java        ✅ Steps 4-5
└── controller/
    └── KycDebugController.java ✅ Step 6

src/test/java/com/theociobanoiu/kycmcp/
└── mcp/
    └── KycMcpToolsIntegrationTest.java ✅ Step 7
```

## Success Metrics

- [ ] All 7 MCP tools implemented and working
- [ ] Integration tests passing
- [ ] Can demo client search, creation, and high-risk identification
- [ ] MCP server properly configured and responding
- [ ] Clean, maintainable code with proper error handling