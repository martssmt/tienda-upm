package es.upm.etsisi.poo.app3.data.repositories.map;

import es.upm.etsisi.poo.app3.data.model.user.User;
import es.upm.etsisi.poo.app3.data.repositories.RepositoryUser;

import java.util.*;

public abstract class RepositoryUserMap<T extends User> implements RepositoryUser<T> {

    protected final Map<String, T> map;
    protected String id;

    public RepositoryUserMap() {
        this.map = new HashMap<>();
        this.id = "";
    }

    @Override
    public void add(T entity, String id) {
        this.map.put(id, entity);
        entity.setId(id);
    }

    @Override
    public void remove(String id) {
        this.map.remove(id);
    }

    @Override
    public List<T> list() {
        return new ArrayList<>(this.map.values());
    }

    @Override
    public T findById(String id) {
        return this.map.get(id);
    }

    @Override
    public T findByMail(String mail) {

        boolean found = false;
        T entity = null;
        Iterator<T> iterator = this.map.values().iterator();

        while (iterator.hasNext() && !found) {
            T actualEntity = iterator.next();
            String actualMail = actualEntity.getMail();
            if (mail.equals(actualMail)) {
                found = true;
                entity = actualEntity;
            }
        }
        return entity;
    }
}

