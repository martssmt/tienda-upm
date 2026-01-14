package es.upm.etsisi.poo.app3.data.repositories.hibernate;

import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;
import es.upm.etsisi.poo.app3.data.model.shop.products.ServiceProduct;
import es.upm.etsisi.poo.app3.data.repositories.PurchasableRepository;
import jakarta.persistence.EntityManager;

public class PurchasableRepositoryHibernate extends RepositoryShopHibernate<Purchasable<Object>, Object> implements PurchasableRepository {

    @SuppressWarnings("unchecked")
    public PurchasableRepositoryHibernate() {
        super((Class<Purchasable<Object>>) (Class<?>) Purchasable.class);
    }

    @Override
    public void add(Purchasable purchasable) {
        try (EntityManager em = JPAUtil.em()) {
            em.getTransaction().begin();
            Object generatedId;
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

    @Override
    public boolean find(Purchasable purchasable) {
        try (EntityManager em = JPAUtil.em()) {
            String jpql;
            Long count = 0L;
            if (purchasable instanceof Product product) {
                // Comparamos por nombre y precio
                jpql = "SELECT COUNT(p) FROM Product p WHERE p.name = :name AND p.price = :price";
                count = em.createQuery(jpql, Long.class)
                        .setParameter("name", product.getName())
                        .setParameter("price", product.getPrice())
                        .getSingleResult();

            } else if (purchasable instanceof ServiceProduct serviceProduct) {
                // Comparamos por tipo de servicio y fecha máxima de uso
                jpql = "SELECT COUNT(s) FROM ServiceProduct s WHERE s.serviceType = :type AND s.maxUsageDate = :date";
                count = em.createQuery(jpql, Long.class)
                        .setParameter("type", serviceProduct.getServiceType())
                        .setParameter("date", serviceProduct.getMaxUsageDate())
                        .getSingleResult();
            }
            return count > 0;
        }
    }

    private String findFirstAvailableServiceId(EntityManager em) {
        int i = 1;
        while (true) {
            String candidate = i + "S";
            if (em.find(Purchasable.class, candidate) == null) {
                return candidate;
            }
            i++;
        }
    }

    private Integer findFirstAvailableIntegerId(EntityManager em) {
        int i = 1;
        while (true) {
            if (em.find(Purchasable.class, i) == null) {
                return i;
            }
            i++;
        }
    }
}