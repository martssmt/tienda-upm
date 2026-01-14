package es.upm.etsisi.poo.app3.data.repositories.hibernate;

import es.upm.etsisi.poo.app3.data.model.Entity;
import es.upm.etsisi.poo.app3.data.repositories.RepositoryShop;
import jakarta.persistence.EntityManager;

import java.util.List;

public abstract class RepositoryShopHibernate<T extends Entity<ID>, ID> implements RepositoryShop<T, ID> {

    private final Class<T> entityManager;

    public RepositoryShopHibernate(Class<T> entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void add(T entity, ID id) {
        try (EntityManager em = JPAUtil.em()) {
            em.getTransaction().begin();
            entity.setId(id);
            em.merge(entity);
            em.getTransaction().commit();
        }
    }

    @Override
    public void remove(ID id) {
        try (EntityManager em = JPAUtil.em()) {
            em.getTransaction().begin();
            T entity = em.find(this.entityManager, id);
            if (entity == null)
                em.remove(entity);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<T> list() {
        try (EntityManager em = JPAUtil.em()) {
            String jpql = "SELECT e FROM " + entityManager.getSimpleName() + " e";
            return em.createQuery(jpql, entityManager).getResultList();
        }
    }

    @Override
    public T findById(ID id) {
        try (EntityManager em = JPAUtil.em()) {
            return em.find(entityManager, id);
        }
    }

}
