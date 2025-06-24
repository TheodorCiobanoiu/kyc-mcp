package com.theociobanoiu.kycmcp.repository;

import com.theociobanoiu.kycmcp.model.entities.Client;
import com.theociobanoiu.kycmcp.model.enums.ClientType;
import com.theociobanoiu.kycmcp.model.enums.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByNameContainingIgnoreCase(String name);

    List<Client> findByClientType(ClientType clientType);

    List<Client> findByRiskLevel(RiskLevel riskLevel);

    List<Client> findByRiskLevelOrderByNameAsc(RiskLevel riskLevel);

    @Query("""
        SELECT c FROM Client c WHERE
        (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND 
        (:clientType IS NULL OR c.clientType = :clientType) AND 
        (:riskLevel IS NULL OR c.riskLevel = :riskLevel)""")
    List<Client> searchClients(@Param("name") String name,
        @Param("clientType") ClientType clientType,
        @Param("riskLevel") RiskLevel riskLevel);
}