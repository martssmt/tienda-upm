package es.upm.etsisi.poo.app3.services;

import es.upm.etsisi.poo.app3.data.model.Entity;

import java.util.List;

/**
 * Generic service interface for application-level operations.
 * <p>
 * This interface defines common business operations for entities extending
 * {@link Entity}. Service implementations act as an intermediate layer between
 * the presentation layer and the persistence layer, encapsulating business logic
 * and coordinating repository access.
 * </p>
 *
 * <p>
 * Services typically perform validation, enforce business rules, and delegate
 * persistence operations to the appropriate repositories.
 * </p>
 *
 * @param <T> the entity type managed by the service
 * @author Sofía
 * @version 3.0
 * @see Entity
 */
public interface Service<T extends Entity> {

    /**
     * Adds a new entity to the system using the provided identifier.
     *
     * @param entity the entity to add
     * @param id     the identifier to assign to the entity
     */
    void add(T entity, String id);

    /**
     * Removes an entity from the system by its identifier.
     *
     * @param id the identifier of the entity to remove
     * @return the removed entity, or {@code null} if it was not found
     */
    T remove(String id);

    /**
     * Retrieves all entities managed by this service.
     *
     * @return the list of managed entities
     */
    List<T> list();
}
