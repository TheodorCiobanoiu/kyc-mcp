package com.theociobanoiu.kycmcp.service.impl;

import com.theociobanoiu.kycmcp.model.dto.ClientDTO;
import com.theociobanoiu.kycmcp.model.dto.request.CreateClientRequest;
import com.theociobanoiu.kycmcp.model.entities.Client;
import com.theociobanoiu.kycmcp.model.entities.Person;
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
            log.info("Successfully created client with ID: {} and name: {}",
                    savedClient.getId(), savedClient.getName());
            return ClientDTO.from(savedClient);
        } catch (Exception e) {
            log.error("Error creating client with name '{}': {}", request.name(), e.getMessage(), e);
            throw new RuntimeException("Failed to create client: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ClientDTO> searchClients(String name, RiskLevel riskLevel) {
        log.debug("Searching clients with name: {}, risk: {}", name, riskLevel);

        List<Client> clients = clientRepository.searchClients(name, riskLevel);
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
    public boolean clientExists(Long clientId) {
        if (clientId == null) {
            return false;
        }

        boolean exists = clientRepository.existsById(clientId);
        log.debug("Client ID: {} exists: {}", clientId, exists);
        return exists;
    }
}