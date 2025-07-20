package com.theociobanoiu.kycmcp.model.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A generic, standardized response wrapper for all MCP tool outputs.
 * This class provides a consistent structure for both successful operations (containing data)
 * and failed operations (containing structured error details).
 *
 * @param <T> The type of the data payload for a successful response.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Generates a private constructor for all fields
public class McpServerResponse<T> {
    private final Status status;
    private final T data;
    private final McpServerError error;

    public static <T> McpServerResponse<T> success(T data) {
        return new McpServerResponse<>(Status.SUCCESS, data, null);
    }

    public static <T> McpServerResponse<T> error(String code, String message) {
        return new McpServerResponse<>(Status.ERROR, null, McpServerError.builder().code(code).message(message).build());
    }
}