package es.upm.etsisi.poo.app2.data.repositories.map;

import es.upm.etsisi.poo.app2.data.model.Entity;
import es.upm.etsisi.poo.app2.data.repositories.RepositoryShop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RepositoryShopMap<T extends Entity> implements RepositoryShop<T> {

    protected final Map<Integer, T> map;
    protected Integer id;

    public RepositoryShopMap() {
        this.map = new HashMap<>();
        this.id = 1;
    }

    @Override
    public void add(T entity, Integer id) {
        this.map.put(id, entity);
        entity.setId(id);
    }

    @Override
    public void remove(Integer id) {
        this.map.remove(id);
    }

    @Override
    public List<T> list() {
        return new ArrayList<>(this.map.values());
    }

    @Override
    public T findById(Integer id) {
        return this.map.get(id);
    }
}

