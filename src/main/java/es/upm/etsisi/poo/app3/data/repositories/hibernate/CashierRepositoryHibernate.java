package es.upm.etsisi.poo.app3.data.repositories.hibernate;

import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.data.model.user.Cashier;
import es.upm.etsisi.poo.app3.data.repositories.CashierRepository;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Random;

public class CashierRepositoryHibernate extends RepositoryUserHibernate<Cashier> implements CashierRepository {

    public CashierRepositoryHibernate() {
        super(Cashier.class);
    }

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
            em.merge(cashier);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Ticket> listTickets() {
        try (EntityManager em = JPAUtil.em()) {
            String jpql = "SELECT t FROM Ticket t";
            return em.createQuery(jpql, Ticket.class).getResultList();
        }
    }
}
