package es.upm.etsisi.poo.app3.data.repositories.hibernate;

import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;
import es.upm.etsisi.poo.app3.data.model.shop.products.ServiceProduct;
import es.upm.etsisi.poo.app3.data.repositories.PurchasableRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Hibernate-based implementation of the {@link PurchasableRepository} interface.
 * <p>
 * This repository provides persistence operations for {@link Purchasable} entities
 * using JPA and Hibernate. It supports both products and services under a unified
 * abstraction, handling identifier generation according to the business rules of E3.
 * </p>
 *
 * <p>
 * Identifier generation rules:
 * <ul>
 *   <li><b>Service purchasables</b> ({@link ServiceProduct}) use sequential numeric IDs
 *   ending with {@code 'S'} (e.g., {@code 1S}, {@code 2S}, ...).</li>
 *   <li><b>Product purchasables</b> ({@link Product} and subclasses) use the first
 *   available non-negative integer identifier represented as a string.</li>
 * </ul>
 * </p>
 *
 * <p>
 * The repository also provides a {@link #find(Purchasable)} method that checks for the
 * existence of equivalent purchasables based on domain-relevant attributes instead of
 * relying only on identifiers.
 * </p>
 *
 * @author Marta
 * @version 3.0
 * @see Purchasable
 * @see Product
 * @see ServiceProduct
 * @see PurchasableRepository
 */
public class PurchasableRepositoryHibernate
        extends RepositoryShopHibernate<Purchasable<Object>, Object>
        implements PurchasableRepository {

    /**
     * Creates a new Hibernate-based purchasable repository.
     * <p>
     * Due to Java type erasure, an unchecked cast is required to pass the
     * {@link Purchasable} class literal to the generic base repository.
     * </p>
     */
    @SuppressWarnings("unchecked")
    public PurchasableRepositoryHibernate() {
        super((Class<Purchasable<Object>>) (Class<?>) Purchasable.class);
    }

    /**
     * Persists a new purchasable element.
     * <p>
     * Before persistence, a new identifier is generated according to the item type:
     * services receive the next sequential {@code *S} identifier, while products
     * receive the first available integer identifier.
     * </p>
     *
     * @param purchasable the purchasable element to persist
     */
    @Override
    public void add(Purchasable purchasable) {
        try (EntityManager em = JPAUtil.em()) {
            em.getTransaction().begin();

            String generatedId;
            if (purchasable instanceof ServiceProduct) {
                generatedId = findFirstAvailableServiceId(em);
            } else {
                generatedId = findFirstAvailableIntegerId(em);
            }

            purchasable.setId(generatedId);
            em.persist(purchasable);
            em.getTransaction().commit();
        }
    }

    /**
     * Checks whether an equivalent purchasable element already exists in persistence.
     * <p>
     * The comparison is performed using domain attributes:
     * <ul>
     *   <li>For {@link Product}: name and price.</li>
     *   <li>For {@link ServiceProduct}: service type and maximum usage date.</li>
     * </ul>
     * </p>
     *
     * @param purchasable the purchasable element to search for
     * @return {@code true} if an equivalent element exists, {@code false} otherwise
     */
    @Override
    public boolean find(Purchasable purchasable) {
        try (EntityManager em = JPAUtil.em()) {
            String jpql;
            Long count = 0L;

            if (purchasable instanceof Product product) {
                jpql = "SELECT COUNT(p) FROM Product p WHERE p.name = :name AND p.price = :price";
                count = em.createQuery(jpql, Long.class)
                        .setParameter("name", product.getName())
                        .setParameter("price", product.getPrice())
                        .getSingleResult();

            } else if (purchasable instanceof ServiceProduct serviceProduct) {
                jpql = "SELECT COUNT(s) FROM ServiceProduct s WHERE s.serviceType = :type AND s.maxUsageDate = :date";
                count = em.createQuery(jpql, Long.class)
                        .setParameter("type", serviceProduct.getServiceType())
                        .setParameter("date", serviceProduct.getMaxUsageDate())
                        .getSingleResult();
            }

            return count > 0;
        }
    }

    /**
     * Finds the first available sequential service identifier.
     * <p>
     * Service identifiers follow the format {@code <number>S}. This method queries
     * existing service IDs, extracts the numeric portion, and returns the next
     * sequential identifier.
     * </p>
     *
     * @param em the active entity manager
     * @return the next available service identifier (e.g., {@code 5S})
     */
    private String findFirstAvailableServiceId(EntityManager em) {
        String jpql = "SELECT p.id FROM Purchasable p WHERE CAST(p.id AS string) LIKE '%S'";
        List<String> ids = em.createQuery(jpql, String.class).getResultList();

        int max = ids.stream()
                .map(id -> id.replace("S", ""))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);

        return (max + 1) + "S";
    }

    /**
     * Finds the first available integer identifier for products.
     * <p>
     * Product identifiers are numeric strings. This method retrieves all current IDs,
     * converts numeric IDs to integers, and returns the first non-negative integer
     * not present in the set.
     * </p>
     *
     * @param em the active entity manager
     * @return the first available numeric identifier as a string
     */
    private String findFirstAvailableIntegerId(EntityManager em) {
        String jpql = "SELECT p.id FROM Product p";
        List<String> currentIds = em.createQuery(jpql, String.class).getResultList();

        java.util.Set<Integer> numericIds = currentIds.stream()
                .filter(id -> id != null && id.matches("\\d+"))
                .map(Integer::parseInt)
                .collect(java.util.stream.Collectors.toSet());

        int candidate = 0;
        while (numericIds.contains(candidate)) {
            candidate++;
        }

        return String.valueOf(candidate);
    }
}