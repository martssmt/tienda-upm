package es.upm.etsisi.poo.app3.data.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.Objects;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class Entity<T> {

    @Id
    protected T id;

    protected Entity() {
        this.id = null;
    }

    public T getId() {
        return this.id;
    }

    public abstract void setId(T id);

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

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "Entity{id=" + this.id + "}";
    }
}
