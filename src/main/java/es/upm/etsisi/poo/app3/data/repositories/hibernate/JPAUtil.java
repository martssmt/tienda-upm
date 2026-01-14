package es.upm.etsisi.poo.app3.data.repositories.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JPAUtil {
    private static final EntityManagerFactory EMF = Persistence.
            createEntityManagerFactory("app3");

    private JPAUtil() {}

    public static EntityManager em() {
        return EMF.createEntityManager();
    }

    public static void shutdown() {
        if (EMF.isOpen()) {
            EMF.close();
        }
    }
}