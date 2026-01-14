package es.upm.etsisi.poo.app3.data.repositories;

import es.upm.etsisi.poo.app3.data.model.Entity;

import java.util.List;

public interface RepositoryShop<T extends Entity<?>, ID> {

    void add(T entity, ID id);

    void remove(ID id);

    List<T> list();

    T findById(ID id);

}
