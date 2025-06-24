package com.theociobanoiu.kycmcp.service.api;

import com.theociobanoiu.kycmcp.model.dto.ClientDTO;
import com.theociobanoiu.kycmcp.model.dto.request.CreateClientRequest;
import com.theociobanoiu.kycmcp.model.enums.ClientType;
import com.theociobanoiu.kycmcp.model.enums.RiskLevel;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing KYC client operations. Provides methods for client CRUD operations, searching, and
 * business logic.
 */
public interface ClientService {

    /**
     * Creates a new client in the system based on the provided request data. The request must contain all required
     * client information as specified in CreateClientRequest.
     *
     * @param request the request object containing client creation data (must not be null)
     * @return The created client in a {@link ClientDTO} format
     * @throws jakarta.validation.ConstraintViolationException if the request contains invalid data
     * @throws IllegalArgumentException                        if the request contains conflicting or invalid business
     *                                                         rules
     */
    ClientDTO createClient(@NotNull CreateClientRequest request);

    /**
     * Search for clients by name, type, or risk level. All parameters are optional - null values are ignored in the
     * search.
     *
     * @param name       partial name to search for (case-insensitive)
     * @param clientType specific client type to filter by
     * @param riskLevel  specific risk level to filter by
     * @return list of matching clients
     */
    List<ClientDTO> searchClients(String name, ClientType clientType, RiskLevel riskLevel);

    /**
     * Retrieve full client information with associated persons.
     *
     * @param clientId the client's unique identifier
     * @return client with all associated persons, or empty if not found
     */
    Optional<ClientDTO> getClientDetails(Long clientId);

    /**
     * Retrieve all clients in the system.
     *
     * @return list of all clients without persons data
     */
    List<ClientDTO> getAllClients();

    /**
     * Retrieve all high-risk clients for compliance review. Results are ordered by client name for easier review.
     *
     * @return list of high-risk clients ordered by name
     */
    List<ClientDTO> getHighRiskClients();

    /**
     * Update a client's risk level based on a new assessment.
     *
     * @param clientId     the client's unique identifier
     * @param newRiskLevel the updated risk level
     * @return the updated client, or empty if a client not found
     */
    Optional<ClientDTO> updateClientRiskLevel(Long clientId, RiskLevel newRiskLevel);

    /**
     * Check if a client exists by ID. Useful for validation before performing operations.
     *
     * @param clientId the client's unique identifier
     * @return true if client exists, false otherwise
     */
    boolean clientExists(Long clientId);

    /**
     * Get all clients of a specific type.
     *
     * @param clientType the type to filter by
     * @return list of clients matching the type
     */
    List<ClientDTO> getClientsByType(ClientType clientType);

    /**
     * Get all clients with a specific risk level.
     *
     * @param riskLevel the risk level to filter by
     * @return list of clients matching the risk level
     */
    List<ClientDTO> getClientsByRiskLevel(RiskLevel riskLevel);
}