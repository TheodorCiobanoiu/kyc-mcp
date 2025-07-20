package com.theociobanoiu.kycmcp.mcp.tools;

import com.theociobanoiu.kycmcp.model.dto.ClientDTO;
import com.theociobanoiu.kycmcp.model.enums.RiskLevel;
import com.theociobanoiu.kycmcp.service.api.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MCP Tools for KYC operations.
 * These tools can be called by Claude or any MCP client to interact with the KYC system.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KycMcpTools {

    private final ClientService clientService;

    /**
     * Search for clients by name, type, or risk level.
     * All parameters are optional - Claude can call with any combination.
     */
    @Tool(name = "search_clients",
            description = """
                    Search for KYC clients by name (partial match), or risk level. \
                    All parameters are optional. Returns a list of matching clients with basic information.""")
    public Map<String, Object> searchClients(String name, RiskLevel riskLevel) {
        log.info("MCP Tool 'search_clients' called with name='{}', riskLevel='{}'",
                name, riskLevel);

        try {
            // Call the service
            List<ClientDTO> clients = clientService.searchClients(name, riskLevel);

            // Build standardized response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", String.format("Found %d clients matching search criteria", clients.size()));
            response.put("count", clients.size());
            response.put("clients", clients);
            response.put("searchCriteria", Map.of(
                    "name", name != null ? name : "any",
                    "riskLevel", riskLevel != null ? riskLevel : "any"
            ));

            log.info("MCP Tool 'search_clients' completed successfully. Found {} clients", clients.size());
            return response;

        } catch (Exception e) {
            log.error("Error in MCP tool 'search_clients': {}", e.getMessage(), e);
            return createErrorResponse("search_clients", e.getMessage());
        }
    }

    /**
     * Helper method to create standardized error responses
     */
    private Map<String, Object> createErrorResponse(String toolName, String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", errorMessage);
        response.put("tool", toolName);
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }
}