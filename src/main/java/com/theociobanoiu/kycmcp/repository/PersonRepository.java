package com.theociobanoiu.kycmcp.repository;

import com.theociobanoiu.kycmcp.model.entities.Person;
import com.theociobanoiu.kycmcp.model.enums.RelationshipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByClientId(Long clientId);

    List<Person> findByRelationshipType(RelationshipType relationshipType);

    @Query("""
        SELECT p FROM Person p WHERE
        LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%'))""")
    List<Person> findByFullNameContaining(@Param("fullName") String fullName);

    @Query("SELECT p FROM Person p WHERE p.relationshipType = 'BENEFICIAL_OWNER'")
    List<Person> findAllBeneficialOwners();
}