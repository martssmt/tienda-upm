package es.upm.etsisi.poo.app3.data.repositories.hibernate;

import es.upm.etsisi.poo.app3.data.model.Entity;
import es.upm.etsisi.poo.app3.data.repositories.RepositoryShop;
import jakarta.persistence.EntityManager;

import java.util.List;

public abstract class RepositoryShopHibernate<T extends Entity<ID>, ID> implements RepositoryShop<T, ID> {

    private final Class<T> entityClass;

    public RepositoryShopHibernate(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void add(T entity, ID id) {
        try (EntityManager em = JPAUtil.em()) {
            Object idProcesado = id;
            if (id instanceof String strId && strId.matches("\\d+")) {
                idProcesado = Integer.parseInt(strId);
            }
            em.getTransaction().begin();
            entity.setId((ID) idProcesado);
            em.merge(entity);
            em.getTransaction().commit();
        }
    }

    @Override
    public void remove(ID id) {
        try (EntityManager em = JPAUtil.em()) {
            em.getTransaction().begin();
            T entity = em.find(this.entityClass, id);
            if (entity != null)
                em.remove(entity);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<T> list() {
        try (EntityManager em = JPAUtil.em()) {
            String jpql = "SELECT e FROM " + this.entityClass.getSimpleName() + " e";
            return em.createQuery(jpql, this.entityClass).getResultList();
        }
    }

    @Override
    public T findById(ID id) {
        try (EntityManager em = JPAUtil.em()) {
            return em.find(this.entityClass, id);
        }
    }

}
