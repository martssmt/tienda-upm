package es.upm.etsisi.poo.app3.data.repositories;

import es.upm.etsisi.poo.app3.data.model.user.User;

import java.util.List;

/**
 * Generic repository interface for {@link User}-based entities.
 * <p>
 * This interface defines common persistence operations for users in the system,
 * such as clients and cashiers. It serves as a base contract for user-specific
 * repositories.
 * </p>
 *
 * <p>
 * Implementations are responsible for managing identifier assignment and
 * interaction with the underlying persistence mechanism.
 * </p>
 *
 * @param <T> the concrete user subtype managed by the repository
 * @author Marta
 * @version 3.0
 * @see User
 */
public interface RepositoryUser<T extends User> {

    /**
     * Adds or updates a user entity using the provided identifier.
     *
     * @param entity the user entity to persist or update
     * @param id     the identifier to assign to the user
     */
    void add(T entity, String id);

    /**
     * Removes a user entity from persistence by its identifier.
     *
     * @param id the identifier of the user to remove
     */
    void remove(String id);

    /**
     * Updates an existing user entity in persistence.
     *
     * @param entity the user entity to update
     */
    void update(T entity);

    /**
     * Retrieves all user entities managed by this repository.
     *
     * @return the list of stored users
     */
    List<T> list();

    /**
     * Finds a user entity by its identifier.
     *
     * @param id the identifier of the user
     * @return the user entity if found, or {@code null} otherwise
     */
    T findById(String id);
}
