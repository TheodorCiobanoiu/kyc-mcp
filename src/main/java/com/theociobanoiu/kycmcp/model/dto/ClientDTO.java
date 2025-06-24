package com.theociobanoiu.kycmcp.model.dto;

import com.theociobanoiu.kycmcp.model.entities.Client;
import com.theociobanoiu.kycmcp.model.entities.Person;
import com.theociobanoiu.kycmcp.model.enums.ClientType;
import com.theociobanoiu.kycmcp.model.enums.RiskLevel;

import java.time.LocalDateTime;
import java.util.List;


public record ClientDTO(
    Long id,
    String name,
    ClientType clientType,
    String email,
    String phone,
    String registrationNumber,
    RiskLevel riskLevel,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<PersonDTO> persons
) {

    // Factory method for creating from entity without persons
    public static ClientDTO from(Client client) {
        return new ClientDTO(
            client.getId(),
            client.getName(),
            client.getClientType(),
            client.getEmail(),
            client.getPhone(),
            client.getRegistrationNumber(),
            client.getRiskLevel(),
            client.getCreatedAt(),
            client.getUpdatedAt(),
            null
        );
    }

    // Factory method for creating from entity with persons
    public static ClientDTO fromWithPersons(Client client, List<Person> persons) {
        return new ClientDTO(
            client.getId(),
            client.getName(),
            client.getClientType(),
            client.getEmail(),
            client.getPhone(),
            client.getRegistrationNumber(),
            client.getRiskLevel(),
            client.getCreatedAt(),
            client.getUpdatedAt(),
            persons.stream().map(PersonDTO::from).toList()
        );
    }

    // Factory method for creating a list from entities
    public static List<ClientDTO> fromList(List<Client> clients) {
        return clients.stream()
            .map(ClientDTO::from)
            .toList();
    }

    // Method to convert back to entity (for creation)
    public Client toEntity() {
        Client client = new Client();
        client.setName(this.name);
        client.setClientType(this.clientType);
        client.setEmail(this.email);
        client.setPhone(this.phone);
        client.setRegistrationNumber(this.registrationNumber);
        client.setRiskLevel(this.riskLevel);
        return client;
    }

    // Convenience method to create without persons (most common case)
    public ClientDTO withoutPersons() {
        return new ClientDTO(id, name, clientType, email, phone, registrationNumber,
            riskLevel, createdAt, updatedAt, null);
    }

    // Convenience method to add persons to existing DTO
    public ClientDTO withPersons(List<PersonDTO> persons) {
        return new ClientDTO(id, name, clientType, email, phone, registrationNumber,
            riskLevel, createdAt, updatedAt, persons);
    }
}