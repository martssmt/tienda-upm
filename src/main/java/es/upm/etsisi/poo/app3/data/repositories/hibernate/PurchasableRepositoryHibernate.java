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
        String jpql = "SELECT p.id FROM Purchasable p WHERE CAST(p.id AS string) LIKE '%S'";
        List<String> ids = em.createQuery(jpql, String.class).getResultList();

        // Extraemos los números, buscamos el máximo y sumamos 1
        int max = ids.stream()
                .map(id -> id.replace("S", ""))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);

        return (max + 1) + "S";
    }

    private String findFirstAvailableIntegerId(EntityManager em) {
        // Obtenemos todos los IDs de los productos actuales
        String jpql = "SELECT p.id FROM Product p";
        List<String> currentIds = em.createQuery(jpql, String.class).getResultList();
        // Los pasamos a un Set de Integers para buscar rápidamente
        java.util.Set<Integer> numericIds = currentIds.stream()
                .filter(id -> id != null && id.matches("\\d+"))
                .map(Integer::parseInt)
                .collect(java.util.stream.Collectors.toSet());
        // Buscamos el primer número (0, 1, 2...) que NO esté en el set
        int candidate = 0;
        while (numericIds.contains(candidate)) {
            candidate++;
        }
        return String.valueOf(candidate);
    }
}