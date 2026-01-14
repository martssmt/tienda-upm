package es.upm.etsisi.poo.app3.data.repositories.hibernate;

import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;
import es.upm.etsisi.poo.app3.data.model.shop.products.ServiceProduct;
import es.upm.etsisi.poo.app3.data.repositories.PurchasableRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

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
        // Buscamos el ID más alto que termine en 'S'
        String jpql = "SELECT p.id FROM Purchasable p WHERE p.id LIKE '%S'";
        List<String> ids = em.createQuery(jpql, String.class).getResultList();

        // Extraemos los números, buscamos el máximo y sumamos 1
        int max = ids.stream()
                .map(id -> id.replace("S", ""))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);

        return (max + 1) + "S";
    }

    private Integer findFirstAvailableIntegerId(EntityManager em) {
        // Buscamos el máximo ID numérico directamente
        String jpql = "SELECT MAX(p.id) FROM Purchasable p WHERE TYPE(p) = Product";
        Integer max = em.createQuery(jpql, Integer.class).getSingleResult();

        return (max == null) ? 1 : max + 1;
    }
}