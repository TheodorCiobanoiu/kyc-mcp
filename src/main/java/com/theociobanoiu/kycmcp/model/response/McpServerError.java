package com.theociobanoiu.kycmcp.model.response;

import lombok.Builder;

/**
 * Represents a structured error response from an MCP tool.
 * Provides a machine-readable code and a human-readable message for error details.
 *
 * @param code    A machine-readable error code (e.g., "NOT_FOUND", "INVALID_INPUT", "INTERNAL_SERVER_ERROR").
 * @param message A human-readable description of the error.
 */
@Builder
public record McpServerError(String code, String message) {
}