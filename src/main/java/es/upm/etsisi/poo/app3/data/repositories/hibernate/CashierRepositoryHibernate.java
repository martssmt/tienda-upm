package es.upm.etsisi.poo.app3.data.repositories.hibernate;

import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.data.model.user.Cashier;
import es.upm.etsisi.poo.app3.data.repositories.CashierRepository;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Random;

/**
 * Hibernate-based implementation of the {@link CashierRepository} interface.
 * <p>
 * This repository provides persistence operations for {@link Cashier} entities
 * using JPA and Hibernate. It extends {@link RepositoryUserHibernate} to reuse
 * common user-related persistence logic.
 * </p>
 *
 * <p>
 * Cashier identifiers are generated automatically following the format
 * {@code UWXXXXXXX}, ensuring uniqueness at persistence time.
 * </p>
 *
 * @author Marta
 * @version 3.0
 * @see Cashier
 * @see CashierRepository
 */
public class CashierRepositoryHibernate
        extends RepositoryUserHibernate<Cashier>
        implements CashierRepository {

    /**
     * Creates a new Hibernate-based cashier repository.
     */
    public CashierRepositoryHibernate() {
        super(Cashier.class);
    }

    /**
     * Persists a new cashier in the database.
     * <p>
     * A unique cashier identifier is automatically generated and assigned
     * before persistence.
     * </p>
     *
     * @param cashier the cashier to persist
     */
    @Override
    public void add(Cashier cashier) {
        try (EntityManager em = JPAUtil.em()) {
            em.getTransaction().begin();

            Random random = new Random();
            String generatedId;
            do {
                int number = random.nextInt(10000000);
                generatedId = String.format("UW%07d", number);
            } while (em.find(Cashier.class, generatedId) != null);

            cashier.setId(generatedId);
            em.persist(cashier);
            em.getTransaction().commit();
        }
    }

    /**
     * Retrieves all tickets stored in the system.
     *
     * @return the list of tickets
     */
    @Override
    public List<Ticket> listTickets() {
        try (EntityManager em = JPAUtil.em()) {
            String jpql = "SELECT t FROM Ticket t";
            return em.createQuery(jpql, Ticket.class).getResultList();
        }
    }
}
