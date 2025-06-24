
package com.theociobanoiu.kycmcp.model.dto.request;

import com.theociobanoiu.kycmcp.model.entities.Client;
import com.theociobanoiu.kycmcp.model.enums.ClientType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating a new client in the KYC system.
 *
 * @param name               client name (required)
 * @param clientType         type of client (required)
 * @param email              client email (optional)
 * @param phone              client phone (optional)
 * @param registrationNumber client registration number (optional)
 */
public record CreateClientRequest(
    @NotBlank(message = "Client name is required")
    String name,

    @NotNull(message = "Client type is required")
    ClientType clientType,

    @Email(message = "Invalid email format")
    String email,

    String phone,

    String registrationNumber
) {

    /**
     * Factory method for creating from a {@link Client} entity
     */
    public CreateClientRequest from(Client client) {
        return new CreateClientRequest(
            client.getName(),
            client.getClientType(),
            client.getEmail(),
            client.getPhone(),
            client.getRegistrationNumber()
        );
    }

    /**
     * Converts this DTO to a Client entity
     */
    public Client toEntity() {
        Client client = new Client();
        client.setName(name != null ? name.trim() : null);
        client.setClientType(clientType);
        client.setEmail(email != null ? email.trim() : null);
        client.setPhone(phone != null ? phone.trim() : null);
        client.setRegistrationNumber(registrationNumber != null ? registrationNumber.trim() : null);
        return client;
    }
}