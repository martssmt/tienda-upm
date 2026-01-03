package es.upm.etsisi.poo.app3.data.repositories.map;

import es.upm.etsisi.poo.app3.data.model.user.Client;
import es.upm.etsisi.poo.app3.data.repositories.ClientRepository;

public class ClientRepositoryMap extends RepositoryUserMap<Client> implements ClientRepository {

    public ClientRepositoryMap() {
        super();
    }

}

