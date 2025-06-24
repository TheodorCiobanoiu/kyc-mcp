package com.theociobanoiu.kycmcp.model.dto;

import com.theociobanoiu.kycmcp.model.entities.Person;
import com.theociobanoiu.kycmcp.model.enums.RelationshipType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PersonDTO(
    Long id,
    String firstName,
    String lastName,
    String fullName,
    String email,
    String phone,
    LocalDate dateOfBirth,
    String nationality,
    String identificationNumber,
    RelationshipType relationshipType,
    String positionTitle,
    BigDecimal ownershipPercentage,
    String address,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long clientId,
    String clientName
) {

    // Factory method for creating from entity
    public static PersonDTO from(Person person) {
        return new PersonDTO(
            person.getId(),
            person.getFirstName(),
            person.getLastName(),
            person.getFullName(),
            person.getEmail(),
            person.getPhone(),
            person.getDateOfBirth(),
            person.getNationality(),
            person.getIdentificationNumber(),
            person.getRelationshipType(),
            person.getPositionTitle(),
            person.getOwnershipPercentage(),
            person.getAddress(),
            person.getCreatedAt(),
            person.getUpdatedAt(),
            person.getClient().getId(),
            person.getClient().getName()
        );
    }

    // Factory method for creating a list from entities
    public static List<PersonDTO> fromList(List<Person> persons) {
        return persons.stream()
            .map(PersonDTO::from)
            .toList();
    }

    // Method to convert back to entity (for creation)
    public Person toEntity() {
        Person person = new Person();
        person.setFirstName(this.firstName);
        person.setLastName(this.lastName);
        person.setEmail(this.email);
        person.setPhone(this.phone);
        person.setDateOfBirth(this.dateOfBirth);
        person.setNationality(this.nationality);
        person.setIdentificationNumber(this.identificationNumber);
        person.setRelationshipType(this.relationshipType);
        person.setPositionTitle(this.positionTitle);
        person.setOwnershipPercentage(this.ownershipPercentage);
        person.setAddress(this.address);
        // Note: client will be set by the service layer
        return person;
    }

    // Convenience constructor for creation (without IDs and timestamps)
    public static PersonDTO forCreation(
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate dateOfBirth,
        String nationality,
        String identificationNumber,
        RelationshipType relationshipType,
        String positionTitle,
        BigDecimal ownershipPercentage,
        String address) {

        return new PersonDTO(
            null, // id
            firstName,
            lastName,
            firstName + " " + lastName, // fullName
            email,
            phone,
            dateOfBirth,
            nationality,
            identificationNumber,
            relationshipType,
            positionTitle,
            ownershipPercentage,
            address,
            null, // createdAt
            null, // updatedAt
            null, // clientId
            null  // clientName
        );
    }
}