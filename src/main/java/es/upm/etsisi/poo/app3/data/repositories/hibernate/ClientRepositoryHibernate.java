package es.upm.etsisi.poo.app3.data.repositories.hibernate;

import es.upm.etsisi.poo.app3.data.model.user.Client;
import es.upm.etsisi.poo.app3.data.repositories.ClientRepository;

public class ClientRepositoryHibernate extends RepositoryUserHibernate<Client> implements ClientRepository {

    public ClientRepositoryHibernate() {
        super(Client.class);
    }

}