package es.upm.etsisi.poo.app3.data.repositories.hibernate;

import es.upm.etsisi.poo.app3.data.model.Entity;
import es.upm.etsisi.poo.app3.data.repositories.RepositoryShop;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Abstract Hibernate/JPA base repository for shop-related persistent entities.
 * <p>
 * This class provides common CRUD operations for entities extending {@link Entity},
 * using an {@link EntityManager} obtained from {@link JPAUtil}. It implements the
 * {@link RepositoryShop} contract and can be extended by concrete repositories to
 * avoid duplicating persistence boilerplate.
 * </p>
 *
 * <p>
 * The repository is parametrized with the entity type {@code T} and its identifier
 * type {@code ID}. The entity class reference is stored to enable generic JPA
 * operations such as {@code find} and JPQL queries based on the entity name.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * public class ProductRepositoryHibernate
 *         extends RepositoryShopHibernate<Product, String> {
 *     public ProductRepositoryHibernate() {
 *         super(Product.class);
 *     }
 * }
 * }</pre>
 *
 * @param <T>  the entity type managed by the repository
 * @param <ID> the identifier type of the entity
 * @author Marta
 * @version 3.0
 * @see RepositoryShop
 * @see JPAUtil
 * @see EntityManager
 */
public abstract class RepositoryShopHibernate<T extends Entity<ID>, ID> implements RepositoryShop<T, ID> {

    /**
     * Runtime class reference of the managed entity type.
     * <p>
     * It is required to execute generic JPA operations such as {@code find}
     * and JPQL queries using the entity name.
     * </p>
     */
    private final Class<T> entityClass;

    /**
     * Creates a new repository for the given entity class.
     *
     * @param entityClass the runtime class of the entity type
     */
    public RepositoryShopHibernate(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Adds or updates an entity in persistence using the provided identifier.
     * <p>
     * The entity identifier is assigned through {@link Entity#setId(Object)}.
     * If no entity with the given identifier exists, the entity is persisted.
     * Otherwise, it is merged (updated).
     * </p>
     *
     * @param entity the entity to persist or update
     * @param id     the identifier to assign and use for lookup
     */
    @Override
    public void add(T entity, ID id) {
        try (EntityManager em = JPAUtil.em()) {
            em.getTransaction().begin();
            entity.setId(id);

            T existing = em.find(entityClass, id);
            if (existing == null) {
                em.persist(entity);
            } else {
                em.merge(entity);
            }

            em.getTransaction().commit();
        }
    }

    /**
     * Removes an entity from persistence by its identifier.
     * <p>
     * If the entity does not exist, no action is performed.
     * </p>
     *
     * @param id the identifier of the entity to remove
     */
    @Override
    public void remove(ID id) {
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
     * Updates an entity in persistence.
     * <p>
     * This method merges the given entity into the current persistence context.
     * </p>
     *
     * @param entity the entity to update
     */
    @Override
    public void update(T entity) {
        try (EntityManager em = JPAUtil.em()) {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        }
    }

    /**
     * Retrieves all entities of the managed type from persistence.
     *
     * @return the list of stored entities
     */
    @Override
    public List<T> list() {
        try (EntityManager em = JPAUtil.em()) {
            String jpql = "SELECT e FROM " + this.entityClass.getSimpleName() + " e";
            return em.createQuery(jpql, this.entityClass).getResultList();
        }
    }

    /**
     * Finds an entity by its identifier.
     *
     * @param id the identifier of the entity
     * @return the entity if found, or {@code null} otherwise
     */
    @Override
    public T findById(ID id) {
        try (EntityManager em = JPAUtil.em()) {
            return em.find(this.entityClass, id);
        }
    }
}