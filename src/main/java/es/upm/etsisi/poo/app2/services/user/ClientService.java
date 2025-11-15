package es.upm.etsisi.poo.app2.services.user;

import es.upm.etsisi.poo.app2.data.model.user.Client;
import es.upm.etsisi.poo.app2.data.repositories.ClientRepository;
import es.upm.etsisi.poo.app2.presentation.view.View;
import es.upm.etsisi.poo.app2.services.Service;
import es.upm.etsisi.poo.app2.services.exceptions.DuplicateException;
import es.upm.etsisi.poo.app2.services.exceptions.NotFoundException;

public class ClientService implements Service<Client> {

    private final ClientRepository clientRepository;
    private final View view;

    public ClientService(ClientRepository clientRepository, View view) {
        this.clientRepository = clientRepository;
        this.view = view;
    }

    @Override
    public void add(Client client, String id) {
        if (this.clientRepository.findById(id) != null) {
            throw new DuplicateException("There is already a client with DNI " + id + " in the Catalog.");
        }
        this.clientRepository.add(client, id);
    }

    @Override
    public void remove(String id) {
        if (this.clientRepository.findById(id) == null) {
            throw new NotFoundException("There is no client with id " + id + " in the Catalog.");
        }
        this.clientRepository.remove(id);
    }

    @Override
    public void list() {
        this.view.showList("Client:",this.clientRepository.list());
    }

}
