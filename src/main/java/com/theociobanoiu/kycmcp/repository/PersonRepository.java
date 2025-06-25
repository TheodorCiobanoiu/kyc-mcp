package com.theociobanoiu.kycmcp.repository;

import com.theociobanoiu.kycmcp.model.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * Find all persons associated with a client
     *
     * @param clientId the ID of the client to search for
     * @return list of persons associated with the specified client
     */
    List<Person> findByClientId(Long clientId);

    /**
     * Find all beneficial owners across all clients
     *
     * @return list of all persons who are beneficial owners
     */
    @Query("SELECT p FROM Person p WHERE p.relationshipType = 'BENEFICIAL_OWNER'")
    List<Person> findAllBeneficialOwners();

}