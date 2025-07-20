# REQ-008 - MCP Server Integration Verification

- **Author**: Theodor Ciobanoiu
- **Status**: To Do
- **Created**: 2025-07-20
- **Last Updated**: 2025-07-20

---

## 1. User Story

```
As a developer,
I want to confirm that the MCP server is properly configured and all my tools are registered,
So that I can be confident the AI agent can discover and use them.
```

### User Persona

- **Primary Persona**: Developer
- **Description**: A developer who has just finished implementing the MCP tools and needs to perform a final sanity
  check on the server's configuration.

## 2. Acceptance Criteria

### Scenario 1: Application Starts Successfully

- **Given** The application code is compiled.
- **When** the application is started using `./mvnw spring-boot:run`.
- **Then** the application starts without any errors or exceptions in the console related to MCP configuration.

### Scenario 2: Verify Tool Registration

- **Given** The application is running.
- **When** a `GET` request is made to the standard MCP endpoint `http://localhost:8080/mcp/tools`.
- **Then** an HTTP `200 OK` response is returned.
- **And** the JSON response body contains a list of all 7 implemented MCP tools (`search_clients`, `get_client_details`,
  etc.).

### Scenario 3: Manually Invoke a Tool via HTTP

- **Given** The application is running and tools are registered.
- **When** a `POST` request with an empty JSON body `{}` is made to `http://localhost:8080/mcp/tools/get_all_clients`.
- **Then** an HTTP `200 OK` response is returned.
- **And** the response body contains the `McpServerResponse` from the tool, indicating a successful call.

## 3. Functional Requirements

### 3.1. MCP Framework Endpoints

This requirement verifies the functionality provided by the Spring AI MCP starter, not functionality we build ourselves.

- **`GET /mcp`**: This endpoint should return basic information about the MCP server.
- **`GET /mcp/tools`**: This endpoint must introspect the application context, find all beans containing `@McpFunction`
  annotations, and return a JSON array describing each registered tool, including its name and description.
- **`POST /mcp/tools/{toolName}`**: This endpoint allows for the direct invocation of a registered tool via an HTTP POST
  request. The request body contains the parameters for the tool, and the response body contains the result from the
  tool.

## 4. Non-Functional Requirements (NFRs)

- **Discoverability**: All 7 MCP tools created in previous steps must be automatically discovered and registered by the
  framework at startup without any manual registration code.

## 5. Technical Notes & Implementation Details

- **Affected Components**: This requirement doesn't involve writing new code but rather verifying the behavior of the
  integrated Spring AI MCP framework.
- **Key Logic**: The verification process is manual. It involves starting the application and using an HTTP client like
  `curl` or Postman to interact with the built-in MCP endpoints.

## 6. Assumptions & Dependencies

- **Dependencies**:
    - Depends on all previous requirements (REQ-000 through REQ-007) being fully implemented.
    - The `spring-ai-mcp-starter` dependency must be correctly configured in the `pom.xml`.

## 7. Implementation Plan

### Implementation Summary

This is a verification-only requirement. No new code will be written. The developer will follow a manual test plan to
ensure the MCP server is behaving as expected.

### Live AI Agent Verification Steps

1. **Start the Database**:
    - *Action*: Run `docker-compose up -d` to ensure the PostgreSQL database is running.
2. **Start the Application**:
    - *Action*: Run `./mvnw spring-boot:run` in a terminal.
    - *Expected Result*: The application starts up, and the console logs show the successful registration of 7 MCP
      tools.
3. **Connect the AI Agent (Gemini)**:
    - *Action*: Configure the Gemini CLI to use the local MCP server as a tool source. This is typically done by setting
      a configuration property that points to `http://localhost:8080/mcp`.
    - *Expected Result*: The Gemini agent will acknowledge the new tool source.
4. **Test Tool Execution via Prompts**:
    - *Action*: Provide natural language prompts to the Gemini agent that would invoke the tools.
        - `"List all clients in the system."` (invokes `get_all_clients`)
        - `"Find the details for client with ID 1."` (invokes `get_client_details`)
        - `"Create a new corporate client named 'Global Tech Inc' with a HIGH risk level."` (invokes `create_client`)
    - *Expected Result*: The Gemini agent successfully calls the tools on the local server and returns the correct,
      formatted `McpServerResponse` as its answer.
