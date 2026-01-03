package es.upm.etsisi.poo.app3.data.repositories;

import es.upm.etsisi.poo.app3.data.model.Entity;

import java.util.List;

public interface RepositoryShop<T extends Entity<?>> {

    void add(T entity, Integer id);

    void remove(Integer id);

    List<T> list();

    T findById(Integer id);

}
