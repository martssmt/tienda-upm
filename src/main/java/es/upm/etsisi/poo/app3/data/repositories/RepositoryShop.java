package es.upm.etsisi.poo.app3.data.repositories;

import es.upm.etsisi.poo.app3.data.model.Entity;

import java.util.List;

/**
 * Generic repository interface for shop-related persistent entities.
 * <p>
 * This interface defines basic CRUD operations for entities extending
 * {@link Entity}. It serves as a common contract for repositories managing
 * products, services, tickets, or other shop domain objects.
 * </p>
 *
 * <p>
 * Concrete implementations (e.g. Hibernate-based repositories) are responsible
 * for providing the actual persistence logic.
 * </p>
 *
 * @param <T>  the entity type managed by the repository
 * @param <ID> the identifier type of the entity
 * @author Marta
 * @version 3.0
 * @see Entity
 */
public interface RepositoryShop<T extends Entity, ID> {

    /**
     * Adds or updates an entity using the provided identifier.
     *
     * @param entity the entity to persist or update
     * @param id     the identifier to assign to the entity
     */
    void add(T entity, ID id);

    /**
     * Removes an entity from persistence by its identifier.
     *
     * @param id the identifier of the entity to remove
     */
    void remove(ID id);

    /**
     * Updates an existing entity in persistence.
     *
     * @param entity the entity to update
     */
    void update(T entity);

    /**
     * Retrieves all entities managed by this repository.
     *
     * @return the list of stored entities
     */
    List<T> list();

    /**
     * Finds an entity by its identifier.
     *
     * @param id the identifier of the entity
     * @return the entity if found, or {@code null} otherwise
     */
    T findById(ID id);
}
