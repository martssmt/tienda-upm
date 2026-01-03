package es.upm.etsisi.poo.app3.services;

import es.upm.etsisi.poo.app3.data.model.user.Client;
import es.upm.etsisi.poo.app3.data.repositories.ClientRepository;
import es.upm.etsisi.poo.app3.services.exceptions.DuplicateException;
import es.upm.etsisi.poo.app3.services.exceptions.NotFoundException;

import java.util.List;

public class ClientService implements Service<Client> {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void add(Client client, String id) {
        if (this.clientRepository.findById(id) != null) {
            throw new DuplicateException("There is already a client with DNI " + id + " registered.");
        }
        this.clientRepository.add(client, id);
    }

    @Override
    public Client remove(String id) {
        Client client = this.clientRepository.findById(id);
        if (client == null) {
            throw new NotFoundException("There is no client with id " + id + " registered.");
        }
        this.clientRepository.remove(id);
        return client;
    }

    @Override
    public List<Client> list() {
        return this.clientRepository.list();
    }

    public Client findById(String id) {
        return this.clientRepository.findById(id);
    }
}