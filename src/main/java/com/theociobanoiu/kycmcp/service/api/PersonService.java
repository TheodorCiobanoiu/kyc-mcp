package com.theociobanoiu.kycmcp.service.api;

import com.theociobanoiu.kycmcp.model.dto.PersonDTO;
import com.theociobanoiu.kycmcp.model.dto.request.CreatePersonRequest;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Service interface for managing KYC person operations.
 * MVP version with only essential methods for Sprint 1.
 */
public interface PersonService {

    /**
     * Add a person to a client using a request DTO.
     * This is the primary method for associating individuals with clients.
     *
     * @param request the request object containing person data (must not be null)
     * @return the created person with full details
     * @throws IllegalArgumentException if required parameters are missing or invalid
     * @throws RuntimeException         if client doesn't exist or creation fails
     */
    PersonDTO addPersonToClient(@NotNull CreatePersonRequest request);

    /**
     * Get all persons associated with a specific client.
     *
     * @param clientId the client's unique identifier
     * @return list of persons associated with the client, empty if none found
     */
    List<PersonDTO> getPersonsByClient(Long clientId);

    /**
     * Find all beneficial owners across all clients.
     * Essential for compliance reporting and risk assessment.
     *
     * @return list of all persons with BENEFICIAL_OWNER relationship type
     */
    List<PersonDTO> getBeneficialOwners();
}