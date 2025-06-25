package com.theociobanoiu.kycmcp.mcp.config;


import com.theociobanoiu.kycmcp.mcp.tools.KycMcpTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for registering MCP tools with the Spring AI MCP server.
 * This configuration automatically discovers and registers all @Tool annotated methods.
 */
@Configuration
@Slf4j
public class McpConfiguration {

    /**
     * Register KYC MCP tools with the Spring AI MCP framework.
     * This bean will automatically scan the KycMcpTools class for @Tool annotations
     * and make them available to MCP clients like Claude.
     */
    @Bean
    public ToolCallbackProvider kycToolCallbackProvider(KycMcpTools kycMcpTools) {
        log.info("Registering KYC MCP tools with Spring AI MCP server");

        ToolCallbackProvider provider = MethodToolCallbackProvider.builder()
                .toolObjects(kycMcpTools)  // Scan this object for @Tool methods
                .build();

        log.info("KYC MCP tools registered successfully");
        return provider;
    }
}