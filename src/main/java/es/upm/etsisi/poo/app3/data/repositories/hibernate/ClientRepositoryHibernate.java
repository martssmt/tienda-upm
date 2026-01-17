package es.upm.etsisi.poo.app3.data.repositories.hibernate;

import es.upm.etsisi.poo.app3.data.model.user.Client;
import es.upm.etsisi.poo.app3.data.repositories.ClientRepository;

/**
 * Hibernate-based implementation of the {@link ClientRepository} interface.
 * <p>
 * This repository provides persistence operations for {@link Client} entities
 * using JPA and Hibernate. It relies on {@link RepositoryUserHibernate} to reuse
 * common user-related persistence logic.
 * </p>
 *
 * @author Marta
 * @version 3.0
 * @see Client
 * @see ClientRepository
 */
public class ClientRepositoryHibernate
        extends RepositoryUserHibernate<Client>
        implements ClientRepository {

    /**
     * Creates a new Hibernate-based client repository.
     */
    public ClientRepositoryHibernate() {
        super(Client.class);
    }
}
