package es.upm.etsisi.poo.app3.data.repositories.hibernate;

import es.upm.etsisi.poo.app3.data.model.user.User;
import es.upm.etsisi.poo.app3.data.repositories.RepositoryUser;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Abstract Hibernate/JPA base repository for {@link User}-based entities.
 * <p>
 * This class provides common CRUD operations for user entities (e.g. {@code Client},
 * {@code Cashier}) using an {@link EntityManager} obtained from {@link JPAUtil}.
 * It implements the {@link RepositoryUser} contract and is intended to be extended
 * by concrete repositories to avoid duplicating persistence boilerplate.
 * </p>
 *
 * <p>
 * The repository stores the runtime entity class to enable generic JPA operations
 * such as {@code find} and JPQL queries based on the entity name.
 * </p>
 *
 * <p>
 * In addition to basic CRUD methods, this repository provides a lookup method
 * by email address, which is commonly required by authentication and user
 * management flows.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * public class ClientRepositoryHibernate extends RepositoryUserHibernate<Client> {
 *     public ClientRepositoryHibernate() {
 *         super(Client.class);
 *     }
 * }
 * }</pre>
 *
 * @param <T> the concrete user subtype managed by the repository
 * @author Marta
 * @version 3.0
 * @see User
 * @see RepositoryUser
 * @see JPAUtil
 * @see EntityManager
 */
public abstract class RepositoryUserHibernate<T extends User> implements RepositoryUser<T> {

    /**
     * Runtime class reference of the managed user entity type.
     */
    private final Class<T> entityClass;

    /**
     * Creates a new repository for the given user entity class.
     *
     * @param entityClass the runtime class of the user entity type
     */
    public RepositoryUserHibernate(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Adds or updates a user entity in persistence using the provided identifier.
     * <p>
     * The identifier is assigned through {@link User#setId(String)}. If no entity
     * with the given identifier exists, the entity is persisted; otherwise it is merged.
     * </p>
     *
     * @param entity the user entity to persist or update
     * @param id     the identifier to assign and use for lookup
     */
    @Override
    public void add(T entity, String id) {
        try (EntityManager em = JPAUtil.em()) {
            em.getTransaction().begin();
            entity.setId(id);

            if (em.find(entityClass, id) == null) {
                em.persist(entity);
            } else {
                em.merge(entity);
            }

            em.getTransaction().commit();
        }
    }

    /**
     * Removes a user entity from persistence by its identifier.
     * <p>
     * If the entity does not exist, no action is performed.
     * </p>
     *
     * @param id the identifier of the user entity to remove
     */
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

    /**
     * Updates a user entity in persistence.
     * <p>
     * This method merges the given entity into the current persistence context.
     * </p>
     *
     * @param entity the user entity to update
     * @throws RuntimeException if the merge operation fails
     */
    @Override
    public void update(T entity) {
        try (EntityManager em = JPAUtil.em()) {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Retrieves all user entities of the managed type from persistence.
     *
     * @return the list of stored user entities
     */
    @Override
    public List<T> list() {
        try (EntityManager em = JPAUtil.em()) {
            String jpql = "SELECT u FROM " + this.entityClass.getSimpleName() + " u";
            return em.createQuery(jpql, this.entityClass).getResultList();
        }
    }

    /**
     * Finds a user entity by its identifier.
     *
     * @param id the identifier of the user entity
     * @return the entity if found, or {@code null} otherwise
     */
    @Override
    public T findById(String id) {
        try (EntityManager em = JPAUtil.em()) {
            return em.find(this.entityClass, id);
        }
    }

    /**
     * Finds a user entity by email address.
     * <p>
     * This method returns the first matching entity if multiple matches exist.
     * If no entity is found, {@code null} is returned.
     * </p>
     *
     * @param mail the email address to search for
     * @return the matching user entity, or {@code null} if not found
     */
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