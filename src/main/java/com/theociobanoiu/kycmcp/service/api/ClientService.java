package com.theociobanoiu.kycmcp.service.api;

import com.theociobanoiu.kycmcp.model.dto.ClientDTO;
import com.theociobanoiu.kycmcp.model.dto.request.CreateClientRequest;
import com.theociobanoiu.kycmcp.model.enums.ClientType;
import com.theociobanoiu.kycmcp.model.enums.RiskLevel;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing KYC client operations.
 * MVP version with only essential methods for Sprint 1.
 */
public interface ClientService {

    /**
     * Creates a new client in the system based on the provided request data.
     *
     * @param request the request object containing client creation data (must not be null)
     * @return The created client in a {@link ClientDTO} format
     * @throws IllegalArgumentException if the request contains invalid data
     */
    ClientDTO createClient(@NotNull CreateClientRequest request);

    /**
     * Search for clients by name, type, or risk level.
     * All parameters are optional - null values are ignored in the search.
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
     * Retrieve all high-risk clients for compliance review.
     * Results are ordered by client name for easier review.
     *
     * @return list of high-risk clients ordered by name
     */
    List<ClientDTO> getHighRiskClients();

    /**
     * Check if a client exists by ID.
     * Essential for PersonService validation.
     *
     * @param clientId the client's unique identifier
     * @return true if client exists, false otherwise
     */
    boolean clientExists(Long clientId);
}