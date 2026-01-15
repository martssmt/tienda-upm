package es.upm.etsisi.poo.app3.data.repositories;

import es.upm.etsisi.poo.app3.data.model.user.User;

import java.util.List;

public interface RepositoryUser<T extends User> {

    void add(T entity, String id);

    void remove(String id);

    void update(T entity);

    List<T> list();

    T findById(String id);

    T findByMail(String mail);

}
