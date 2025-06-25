package com.theociobanoiu.kycmcp.model.dto.request;

import com.theociobanoiu.kycmcp.model.entities.Person;
import com.theociobanoiu.kycmcp.model.enums.RelationshipType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for adding a person to a client in the KYC system.
 */
public record CreatePersonRequest(
        @NotNull(message = "Client ID is required")
        Long clientId,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @Email(message = "Invalid email format")
        String email,

        String phone,

        LocalDate dateOfBirth,

        String nationality,

        String identificationNumber,

        @NotNull(message = "Relationship type is required")
        RelationshipType relationshipType,

        String positionTitle,

        @DecimalMin(value = "0.0", message = "Ownership percentage must be between 0 and 100")
        @DecimalMax(value = "100.0", message = "Ownership percentage must be between 0 and 100")
        BigDecimal ownershipPercentage,

        String address
) {

    /**
     * Converts this DTO to a Person entity (without client relationship)
     */
    public Person toEntity() {
        Person person = new Person();
        person.setFirstName(firstName != null ? firstName.trim() : null);
        person.setLastName(lastName != null ? lastName.trim() : null);
        person.setEmail(email != null ? email.trim() : null);
        person.setPhone(phone != null ? phone.trim() : null);
        person.setDateOfBirth(dateOfBirth);
        person.setNationality(nationality != null ? nationality.trim() : null);
        person.setIdentificationNumber(identificationNumber != null ? identificationNumber.trim() : null);
        person.setRelationshipType(relationshipType);
        person.setPositionTitle(positionTitle != null ? positionTitle.trim() : null);
        person.setOwnershipPercentage(ownershipPercentage);
        person.setAddress(address != null ? address.trim() : null);
        // Note: client will be set by the service layer
        return person;
    }
}