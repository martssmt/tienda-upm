package es.upm.etsisi.poo.app3.data.repositories.hibernate;

import es.upm.etsisi.poo.app3.data.model.user.User;
import es.upm.etsisi.poo.app3.data.repositories.RepositoryUser;
import jakarta.persistence.EntityManager;

import java.util.List;

public abstract class RepositoryUserHibernate<T extends User> implements RepositoryUser<T> {

    private final Class<T> entityClass;

    public RepositoryUserHibernate(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void add(T entity, String id) {
        try (EntityManager em = JPAUtil.em()) {
            em.getTransaction().begin();
            entity.setId(id);
            em.merge(entity);
            em.getTransaction().commit();
        }
    }

    @Override
    public void remove(String id) {
        try (EntityManager em = JPAUtil.em()) {
            em.getTransaction().begin();
            T entity = em.find(this.entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public List<T> list() {
        try (EntityManager em = JPAUtil.em()) {
            String jpql = "SELECT u FROM " + this.entityClass.getSimpleName() + " u";
            return em.createQuery(jpql, this.entityClass).getResultList();
        }
    }

    @Override
    public T findById(String id) {
        try (EntityManager em = JPAUtil.em()) {
            return em.find(this.entityClass, id);
        }
    }

    @Override
    public T findByMail(String mail) {
        try (EntityManager em = JPAUtil.em()) {
            String jpql = "SELECT u FROM " + this.entityClass.getSimpleName() + " u WHERE u.mail = :email";
            List<T> results = em.createQuery(jpql, this.entityClass)
                    .setParameter("email", mail)
                    .getResultList();
            return results.isEmpty() ? null : results.getFirst();
        }
    }
}
