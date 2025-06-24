package com.theociobanoiu.kycmcp.service.impl;

import com.theociobanoiu.kycmcp.model.dto.ClientDTO;
import com.theociobanoiu.kycmcp.model.dto.request.CreateClientRequest;
import com.theociobanoiu.kycmcp.model.entities.Client;
import com.theociobanoiu.kycmcp.model.entities.Person;
import com.theociobanoiu.kycmcp.model.enums.ClientType;
import com.theociobanoiu.kycmcp.model.enums.RiskLevel;
import com.theociobanoiu.kycmcp.repository.ClientRepository;
import com.theociobanoiu.kycmcp.repository.PersonRepository;
import com.theociobanoiu.kycmcp.service.api.ClientService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PersonRepository personRepository;

    @Override
    public ClientDTO createClient(@NotNull CreateClientRequest request) {
        log.debug("Creating new client: {}", request.name());

        Client client = request.toEntity();

        try {
            Client savedClient = clientRepository.save(client);
            log.info("Successfully created client with ID: {} and name: {}", savedClient.getId(),
                savedClient.getName());
            return ClientDTO.from(savedClient);
        } catch (Exception e) {
            log.error("Error creating client with name '{}': {}", request.name(), e.getMessage(), e);
            throw new RuntimeException("Failed to create client: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ClientDTO> searchClients(String name, ClientType clientType, RiskLevel riskLevel) {
        log.debug("Searching clients with name: {}, type: {}, risk: {}", name, clientType, riskLevel);

        List<Client> clients = clientRepository.searchClients(name, clientType, riskLevel);
        log.debug("Found {} clients matching search criteria", clients.size());

        return ClientDTO.fromList(clients);
    }

    @Override
    public Optional<ClientDTO> getClientDetails(Long clientId) {
        log.debug("Getting client details for ID: {}", clientId);

        if (clientId == null) {
            log.warn("Client ID is null");
            return Optional.empty();
        }

        return clientRepository.findById(clientId)
            .map(client -> {
                log.debug("Found client: {}, fetching associated persons", client.getName());
                List<Person> persons = personRepository.findByClientId(clientId);
                log.debug("Found {} persons for client ID: {}", persons.size(), clientId);
                return ClientDTO.fromWithPersons(client, persons);
            });
    }

    @Override
    public List<ClientDTO> getAllClients() {
        log.debug("Getting all clients");

        List<Client> clients = clientRepository.findAll();
        log.debug("Retrieved {} clients from database", clients.size());

        return ClientDTO.fromList(clients);
    }

    @Override
    public List<ClientDTO> getHighRiskClients() {
        log.debug("Getting all high-risk clients");

        List<Client> clients = clientRepository.findByRiskLevelOrderByNameAsc(RiskLevel.HIGH);
        log.debug("Found {} high-risk clients", clients.size());

        return ClientDTO.fromList(clients);
    }


    @Override
    public Optional<ClientDTO> updateClientRiskLevel(Long clientId, RiskLevel newRiskLevel) {
        log.debug("Updating risk level for client ID: {} to {}", clientId, newRiskLevel);

        if (clientId == null) {
            log.warn("Client ID is null for risk level update");
            return Optional.empty();
        }

        if (newRiskLevel == null) {
            throw new IllegalArgumentException("New risk level cannot be null");
        }

        return clientRepository.findById(clientId)
            .map(client -> {
                RiskLevel oldRiskLevel = client.getRiskLevel();
                client.setRiskLevel(newRiskLevel);
                Client savedClient = clientRepository.save(client);
                log.info("Updated client ID: {} risk level from {} to {}",
                    clientId, oldRiskLevel, newRiskLevel);
                return ClientDTO.from(savedClient);
            });
    }

    @Override
    public boolean clientExists(Long clientId) {
        if (clientId == null) {
            return false;
        }

        boolean exists = clientRepository.existsById(clientId);
        log.debug("Client ID: {} exists: {}", clientId, exists);
        return exists;
    }

    @Override
    public List<ClientDTO> getClientsByType(ClientType clientType) {
        log.debug("Getting clients by type: {}", clientType);

        if (clientType == null) {
            log.warn("Client type is null, returning empty list");
            return List.of();
        }

        List<Client> clients = clientRepository.findByClientType(clientType);
        log.debug("Found {} clients with type: {}", clients.size(), clientType);

        return ClientDTO.fromList(clients);
    }

    @Override
    public List<ClientDTO> getClientsByRiskLevel(RiskLevel riskLevel) {
        log.debug("Getting clients by risk level: {}", riskLevel);

        if (riskLevel == null) {
            log.warn("Risk level is null, returning empty list");
            return List.of();
        }

        List<Client> clients = clientRepository.findByRiskLevel(riskLevel);
        log.debug("Found {} clients with risk level: {}", clients.size(), riskLevel);

        return ClientDTO.fromList(clients);
    }

}