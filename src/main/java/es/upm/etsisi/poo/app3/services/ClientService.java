package es.upm.etsisi.poo.app3.services;

import es.upm.etsisi.poo.app3.data.model.user.Client;
import es.upm.etsisi.poo.app3.data.repositories.ClientRepository;
import es.upm.etsisi.poo.app3.services.exceptions.DuplicateException;
import es.upm.etsisi.poo.app3.services.exceptions.NotFoundException;

import java.util.List;

/**
 * Service class responsible for managing {@link Client} entities.
 * <p>
 * This service acts as an application-layer façade over the
 * {@link ClientRepository}, enforcing business rules related to
 * client registration and removal before delegating persistence
 * operations.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Client
 * @see ClientRepository
 */
public class ClientService implements Service<Client> {

    /**
     * Repository used to persist and retrieve client entities.
     */
    private final ClientRepository clientRepository;

    /**
     * Creates a new client service using the given repository.
     *
     * @param clientRepository the client repository to use
     */
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Registers a new client with the given identifier.
     * <p>
     * If a client with the same identifier already exists, a
     * {@link DuplicateException} is thrown.
     * </p>
     *
     * @param client the client to register
     * @param id     the identifier to assign
     * @throws DuplicateException if a client with the same identifier already exists
     */
    @Override
    public void add(Client client, String id) {
        if (this.clientRepository.findById(id) != null) {
            throw new DuplicateException("There is already a client with DNI " + id + " registered.");
        }
        this.clientRepository.add(client, id);
    }

    /**
     * Removes a client from the system by its identifier.
     *
     * @param id the identifier of the client to remove
     * @return the removed client
     * @throws NotFoundException if no client with the given identifier exists
     */
    @Override
    public Client remove(String id) {
        Client client = this.clientRepository.findById(id);
        if (client == null) {
            throw new NotFoundException("There is no client with id " + id + " registered.");
        }
        this.clientRepository.remove(id);
        return client;
    }

    /**
     * Retrieves all registered clients.
     *
     * @return the list of clients
     */
    @Override
    public List<Client> list() {
        return this.clientRepository.list();
    }

    /**
     * Finds a client by its identifier.
     *
     * @param id the client identifier
     * @return the client if found, or {@code null} otherwise
     */
    public Client findById(String id) {
        return this.clientRepository.findById(id);
    }
}