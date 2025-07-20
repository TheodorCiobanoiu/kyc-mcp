# ADR-002: Standardize MCP Tool Response Structure

- **Status**: Accepted
- **Date**: 2025-07-20
- **Authors**: Theodor Ciobanoiu
- **Deciders**: Theodor Ciobanoiu

---

## 1. Context

The initial design for the MCP (Multi-turn Conversation Protocol) tools specified that each tool would return a
`Map<String, Object>`. In the case of an error, the map would contain a key named `"error"` with a descriptive message.

This approach presents several issues:

- **Brittleness**: The client (the AI agent) must manually check for the existence of the `"error"` key in the map. A
  typo in the key name would break the error handling.
- **Lack of Type Safety**: The structure is not type-safe. The compiler cannot enforce the response format, leading to
  potential runtime errors.
- **Inconsistency**: Without a strict contract, different tools might return slightly different error structures,
  increasing the cognitive load on the developer and the complexity of the AI agent's response parsing logic.

We need a robust, standardized, and type-safe way for all MCP tools to communicate their outcomes, whether successful or
not.

## 2. Decision

We will implement a generic, standardized response wrapper class, `McpServerResponse<T>`, for all MCP tool return types.
This class will provide a consistent and predictable structure for both successful and failed operations.
All MCP functions will now have a return signature of `McpServerResponse<Type>`, for example,
`McpServerResponse<ClientDto>` or `McpServerResponse<List<PersonDto>>`.

## 3. Consequences

### 3.1. Positive Consequences

- **Robustness & Type Safety**: The AI agent can now rely on a consistent, type-safe structure. The presence of data or
  an error is explicit and enforced by the type system.
- **Improved Developer Experience**: Developers creating new tools have a clear and standardized pattern to follow for
  returning results.
- **Simplified Client Logic**: The AI agent's logic for handling tool responses is simplified. It can check the `status`
  enum and then deserialize either the `data` or `error` object, without manual key checking.
- **Extensibility**: The `McpServerError` record can be easily extended in the future to include more details (e.g., a
  unique error ID, validation details) without breaking existing clients.

### 3.2. Negative Consequences

- **Slightly More Boilerplate**: Instead of returning a simple `Map`, developers must now instantiate and return an
  `McpServerResponse` object. This is a minor trade-off for the significant gain in robustness.

### 3.3. Neutral Consequences

- This change affects the internal tool layer only. It has no direct impact on the external REST API contracts defined
  in the `controller` layer, which will continue to use `ResponseEntity`.

## 4. Alternatives Considered

- **Returning `Map<String, Object>`**: This was the initial proposal. It was rejected due to its lack of type safety and
  the brittle nature of relying on string keys for error checking.
- **Using `ResponseEntity`**: This was considered as it is a standard for REST controllers. It was rejected because
  `ResponseEntity` is an HTTP-specific class. Using it in the internal tool layer would be a leaky abstraction, coupling
  our business logic to the HTTP protocol unnecessarily.
- **Throwing Exceptions**: Tools could throw custom checked exceptions for failure cases. This was considered but
  rejected because the Spring AI MCP framework is not explicitly designed to handle exceptions as a primary means of
  returning error details to the AI agent. A structured response object is more aligned with how the agent expects to
  consume tool output.

## 5. References

- [REQ-000_Standardized_Mcp_Response.md](<../../requirements/epics/mvp/REQ-000_Standardized_Mcp_Response.md>)

## 6. Enforcement

- **Manual Processes**: This decision will be enforced through code reviews. All new MCP tools must adhere to the
  `McpServerResponse<T>` return type.
- **Documentation**: This ADR and the corresponding requirement document will serve as the official reference for this
  pattern.
