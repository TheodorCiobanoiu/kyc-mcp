package com.theociobanoiu.kycmcp.service.impl;

import com.theociobanoiu.kycmcp.model.dto.PersonDTO;
import com.theociobanoiu.kycmcp.model.dto.request.CreatePersonRequest;
import com.theociobanoiu.kycmcp.model.entities.Client;
import com.theociobanoiu.kycmcp.model.entities.Person;
import com.theociobanoiu.kycmcp.repository.ClientRepository;
import com.theociobanoiu.kycmcp.repository.PersonRepository;
import com.theociobanoiu.kycmcp.service.api.PersonService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final ClientRepository clientRepository;

    @Override
    public PersonDTO addPersonToClient(@NotNull CreatePersonRequest request) {
        log.debug("Adding person {} {} to client ID: {}",
                request.firstName(), request.lastName(), request.clientId());

        // Verify client exists
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + request.clientId()));

        // Create a person entity from request
        Person person = request.toEntity();
        person.setClient(client);

        try {
            Person savedPerson = personRepository.save(person);
            log.info("Successfully added person {} {} (ID: {}) to client {} (ID: {})",
                    savedPerson.getFirstName(), savedPerson.getLastName(), savedPerson.getId(),
                    client.getName(), request.clientId());
            return PersonDTO.from(savedPerson);
        } catch (Exception e) {
            log.error("Error adding person {} {} to client ID {}: {}",
                    request.firstName(), request.lastName(), request.clientId(), e.getMessage(), e);
            throw new RuntimeException("Failed to add person to client: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PersonDTO> getPersonsByClient(Long clientId) {
        log.debug("Getting persons for client ID: {}", clientId);

        if (clientId == null) {
            log.warn("Client ID is null, returning empty list");
            return List.of();
        }

        List<Person> persons = personRepository.findByClientId(clientId);
        log.debug("Found {} persons for client ID: {}", persons.size(), clientId);

        return PersonDTO.fromList(persons);
    }

    @Override
    public List<PersonDTO> getBeneficialOwners() {
        log.debug("Getting all beneficial owners");

        List<Person> persons = personRepository.findAllBeneficialOwners();
        log.debug("Found {} beneficial owners", persons.size());

        return PersonDTO.fromList(persons);
    }
}