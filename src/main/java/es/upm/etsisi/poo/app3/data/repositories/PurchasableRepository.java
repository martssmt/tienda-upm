package es.upm.etsisi.poo.app3.data.repositories;

import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;

/**
 * Repository interface for {@link Purchasable} entities.
 * <p>
 * This interface defines persistence operations for elements that can be added
 * to tickets, including products and services. It extends the generic
 * {@link RepositoryShop} contract and adds domain-specific operations related
 * to purchasable elements.
 * </p>
 *
 * <p>
 * Implementations are responsible for handling identifier generation and
 * ensuring that purchasable elements are uniquely identified and stored.
 * </p>
 *
 * @author Marta
 * @version 3.0
 * @see Purchasable
 * @see RepositoryShop
 */
public interface PurchasableRepository extends RepositoryShop<Purchasable<Object>, Object> {

    /**
     * Persists a new purchasable element.
     *
     * @param purchasable the purchasable element to persist
     */
    void add(Purchasable<Object> purchasable);

    /**
     * Checks whether an equivalent purchasable element already exists.
     *
     * @param purchasable the purchasable element to search for
     * @return {@code true} if an equivalent element exists, {@code false} otherwise
     */
    boolean find(Purchasable<Object> purchasable);
}
