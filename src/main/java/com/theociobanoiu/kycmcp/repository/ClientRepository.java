package com.theociobanoiu.kycmcp.repository;

import com.theociobanoiu.kycmcp.model.entities.Client;
import com.theociobanoiu.kycmcp.model.enums.ClientType;
import com.theociobanoiu.kycmcp.model.enums.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Client entity persistence operations.
 * Provides methods for searching, filtering and retrieving client data.
 * Extends JpaRepository to inherit standard CRUD operations.
 * MVP version with only essential methods for Sprint 1.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Retrieves a list of clients filtered by specific risk level and sorted alphabetically by name.
     *
     * @param riskLevel The risk level to filter clients by
     * @return List of clients matching the specified risk level, ordered by name ascending
     */
    List<Client> findByRiskLevelOrderByNameAsc(RiskLevel riskLevel);

    /**
     * Performs a flexible search for clients based on multiple optional criteria.
     * Any parameter can be null, in which case it won't be used as a filter.
     *
     * @param name       The client name to search for (partial matches supported)
     * @param clientType The type of client to filter by
     * @param riskLevel  The risk level to filter by
     * @return List of clients matching all provided criteria
     */
    @Query("""
        SELECT c FROM Client c WHERE
            (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND
            (:clientType IS NULL OR c.clientType = :clientType) AND
        (:riskLevel IS NULL OR c.riskLevel = :riskLevel)""")
    List<Client> searchClients(@Param("name") String name,
                               @Param("clientType") ClientType clientType,
                               @Param("riskLevel") RiskLevel riskLevel);
}