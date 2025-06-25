package com.theociobanoiu.kycmcp.controller;

import com.theociobanoiu.kycmcp.mcp.tools.KycMcpTools;
import com.theociobanoiu.kycmcp.model.enums.RiskLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Debug controller to test MCP tools via HTTP during development.
 * This allows testing MCP functionality without needing Claude or MCP client.
 */
@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@Slf4j
public class KycDebugController {

    private final KycMcpTools kycMcpTools;

    /**
     * Test the search_clients MCP tool via HTTP GET request.
     * <p>
     * Example calls:
     * GET /api/debug/search-clients
     * GET /api/debug/search-clients?name=ABC
     * GET /api/debug/search-clients?clientType=COMPANY
     * GET /api/debug/search-clients?riskLevel=HIGH
     * GET /api/debug/search-clients?name=corp&clientType=COMPANY&riskLevel=HIGH
     */
    @GetMapping("/search-clients")
    public Map<String, Object> testSearchClients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) RiskLevel riskLevel) {

        log.info("Testing search_clients MCP tool via HTTP: name='{}', riskLevel='{}'",
                name, riskLevel);

        // Call the actual MCP tool method
        return kycMcpTools.searchClients(name, riskLevel);
    }

    /**
     * Get information about available MCP tools
     */
    @GetMapping("/tools")
    public Map<String, Object> getAvailableTools() {
        return Map.of(
                "message", "Available KYC MCP Tools",
                "tools", Map.of(
                        "search_clients", Map.of(
                                "description", "Search for KYC clients by name, type, or risk level",
                                "parameters", Map.of(
                                        "name", "Optional - partial client name to search for",
                                        "clientType", "Optional - client type (INDIVIDUAL, COMPANY, TRUST, PARTNERSHIP, OTHER)",
                                        "riskLevel", "Optional - risk level (LOW, MEDIUM, HIGH)"
                                ),
                                "testUrl", "/api/debug/search-clients?name=ABC&clientType=COMPANY&riskLevel=HIGH"
                        )
                )
        );
    }
}