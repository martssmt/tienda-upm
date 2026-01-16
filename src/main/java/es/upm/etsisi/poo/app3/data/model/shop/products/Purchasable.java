package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.Entity;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.TicketItem;
import jakarta.persistence.*;

/**
 * The {@code Purchasable} abstract class represents any element that can be
 * added to a {@link es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket}.
 * <p>
 * A purchasable defines the minimum contract required by the ticket system:
 * the ability to compute a unit price in a given context and to validate
 * its availability before being added or finalized.
 * </p>
 *
 * <p>
 * This class serves as the root of the purchasable hierarchy, unifying products
 * and services under a common abstraction. Concrete subclasses include
 * {@link Product} and service-related implementations.
 * </p>
 *
 * <p>
 * Purchasables are persisted using a joined inheritance strategy, allowing
 * different specializations to be stored in separate tables while sharing
 * a common identity.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * Purchasable<?> item = product;
 * double price = item.getUnitPrice(ticketItem);
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 * @see Product
 * @see es.upm.etsisi.poo.app3.data.model.shop.products.ServiceProduct
 */
@jakarta.persistence.Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "purchasables")
public abstract class Purchasable<T> extends Entity<T> {

    /**
     * Computes the unit price of the purchasable element in the context
     * of a specific ticket item.
     * <p>
     * The context may affect the final price (e.g., personalized products
     * or dynamic pricing rules).
     * </p>
     *
     * @param context the ticket item providing contextual information
     * @return the unit price
     */
    public abstract double getUnitPrice(TicketItem context);

    /**
     * Validates the availability of the purchasable element.
     * <p>
     * Implementations must enforce any business constraints related to
     * availability (e.g., expiration dates, usage limits, or scheduling rules).
     * </p>
     *
     * @throws RuntimeException if the purchasable element is not available
     */
    public abstract void validateAvailability();
}
