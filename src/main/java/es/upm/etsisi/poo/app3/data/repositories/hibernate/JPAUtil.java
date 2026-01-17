package es.upm.etsisi.poo.app3.data.repositories.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility class for managing JPA {@link EntityManager} instances.
 * <p>
 * This class centralizes the creation and lifecycle management of the
 * {@link EntityManagerFactory} used by the application, ensuring a single
 * persistence unit configuration is shared across all repositories.
 * </p>
 *
 * <p>
 * It provides convenience methods to obtain {@link EntityManager} instances
 * and to properly shut down the persistence layer when the application ends.
 * </p>
 *
 * <p>
 * This class follows the utility class pattern and cannot be instantiated.
 * </p>
 *
 * @author Tomás
 * @version 3.0
 */
public final class JPAUtil {

    /**
     * Shared {@link EntityManagerFactory} for the application.
     */
    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("app3");

    /**
     * Private constructor to prevent instantiation.
     */
    private JPAUtil() {
    }

    /**
     * Creates and returns a new {@link EntityManager}.
     *
     * @return a new entity manager instance
     */
    public static EntityManager em() {
        return EMF.createEntityManager();
    }

    /**
     * Shuts down the {@link EntityManagerFactory} if it is open.
     * <p>
     * This method should be called when the application is terminating
     * to release persistence resources properly.
     * </p>
     */
    public static void shutdown() {
        if (EMF.isOpen()) {
            EMF.close();
        }
    }
}