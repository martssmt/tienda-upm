package es.upm.etsisi.poo.app3.data.repositories;

import es.upm.etsisi.poo.app3.data.model.user.Client;

/**
 * Repository interface for {@link Client} entities.
 * <p>
 * This interface defines persistence operations for clients by extending
 * the generic {@link RepositoryUser} contract. It does not introduce
 * additional methods, serving primarily as a type-safe specialization
 * for client-related repositories.
 * </p>
 *
 * <p>
 * Concrete implementations (e.g. Hibernate-based repositories) are responsible
 * for providing the actual persistence logic.
 * </p>
 *
 * @author Marta
 * @version 3.0
 * @see Client
 * @see RepositoryUser
 */
public interface ClientRepository extends RepositoryUser<Client> {

}
