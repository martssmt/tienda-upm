package es.upm.etsisi.poo.app3.data.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.Objects;

/**
 * Abstract base class for all persistent entities in the system.
 * <p>
 * This class defines a generic identifier {@code id} and provides a common
 * implementation of the {@code equals}, {@code hashCode}, and {@code toString}
 * methods, all of them based exclusively on this identifier.
 * </p>
 *
 * <p>
 * It is annotated as a {@link jakarta.persistence.MappedSuperclass} so that
 * subclasses inherit the identifier field and its persistence configuration
 * without generating a dedicated database table.
 * </p>
 *
 * <p>
 * Field-based access is used via {@link jakarta.persistence.AccessType#FIELD},
 * allowing JPA to access the entity attributes directly.
 * </p>
 *
 * @param <T> the type of the entity identifier
 * @author Jiling
 * @version 3.0
 * @see jakarta.persistence.MappedSuperclass
 * @see jakarta.persistence.Id
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class Entity<T> {

    /**
     * Unique identifier of the entity.
     * <p>
     * This field acts as the primary key in persistence and must be managed
     * by the concrete subclasses.
     * </p>
     */
    @Id
    protected String id;

    /**
     * Protected no-argument constructor required by JPA.
     * <p>
     * Initializes the entity identifier to {@code null}.
     * </p>
     */
    protected Entity() {
        this.id = null;
    }

    /**
     * Returns the identifier of the entity.
     *
     * @return the entity identifier
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the identifier of the entity.
     * <p>
     * The concrete implementation is delegated to subclasses, allowing
     * different identifier generation or validation strategies.
     * </p>
     *
     * @param id the identifier to be assigned
     */
    public abstract void setId(T id);

    /**
     * Compares this entity with another object.
     * <p>
     * Two entities are considered equal if:
     * <ul>
     *   <li>They belong to the same concrete class</li>
     *   <li>They share the same identifier</li>
     * </ul>
     * </p>
     *
     * @param entity the object to compare with
     * @return {@code true} if both entities are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object entity) {
        if (this == entity) {
            return true;
        }
        if (entity == null || this.getClass() != entity.getClass()) {
            return false;
        }
        Entity<?> other = (Entity<?>) entity;
        return Objects.equals(this.id, other.id);
    }

    /**
     * Computes the hash code of the entity.
     * <p>
     * The hash code is generated exclusively from the identifier in order
     * to remain consistent with the {@link #equals(Object)} method.
     * </p>
     *
     * @return the hash code of the entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    /**
     * Returns a string representation of the entity.
     *
     * @return a textual representation of the entity
     */
    @Override
    public String toString() {
        return "Entity{id=" + this.id + "}";
    }
}