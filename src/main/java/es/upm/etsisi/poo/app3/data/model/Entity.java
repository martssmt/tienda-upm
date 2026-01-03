package es.upm.etsisi.poo.app3.data.model;

import java.util.Objects;

public abstract class Entity<T> {
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
