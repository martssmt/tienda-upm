package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.TicketItem;
import jakarta.persistence.*;

/**
 * The {@code Product} class represents an abstract purchasable product
 * in the store system.
 * <p>
 * A product is characterized by a name and a base unit price. Concrete
 * subclasses (such as {@link BasicProduct} or {@link CustomProduct})
 * may extend this behavior by adding category-based discounts or
 * customization logic.
 * </p>
 *
 * <p>
 * Products are persisted using a joined inheritance strategy, allowing
 * different product specializations to be stored in separate tables
 * while sharing a common identity.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * Product product = new BasicProduct("Pen", Category.STATIONERY, 1.50);
 * double unitPrice = product.getUnitPrice(ticketItem);
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 * @see Purchasable
 * @see BasicProduct
 * @see CustomProduct
 */
@jakarta.persistence.Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Product extends Purchasable<String> {

    /**
     * Name of the product.
     */
    @Column(name = "name")
    private String name;

    /**
     * Base unit price of the product.
     */
    @Column(name = "price")
    private Double price;

    /**
     * Protected no-argument constructor required by JPA.
     */
    protected Product() {
        super();
    }

    /**
     * Creates a new product with the given name and price.
     *
     * @param name  the product name
     * @param price the base unit price
     */
    public Product(String name, Double price) {
        super();
        this.name = name;
        this.price = price;
    }

    /**
     * Copy constructor.
     * <p>
     * Creates a new {@code Product} by copying the state of an existing one.
     * </p>
     *
     * @param original the original product to copy
     */
    public Product(Product original) {
        super();
        this.name = original.name;
        this.price = original.price;
    }

    /**
     * Returns the product identifier.
     *
     * @return the product identifier
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the product identifier as an integer.
     *
     * @return the product identifier as an {@link Integer}, or {@code null} if not set
     */
    public Integer getIdAsInt() {
        return id != null ? Integer.parseInt(id) : null;
    }

    /**
     * Returns the product name.
     *
     * @return the product name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the product name.
     *
     * @param name the new product name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the base unit price of the product.
     *
     * @return the unit price
     */
    public Double getPrice() {
        return this.price;
    }

    /**
     * Sets the base unit price of the product.
     *
     * @param price the new unit price
     * @throws InvalidAttributeException if the price is negative
     */
    public void setPrice(Double price) {
        if (price < 0) {
            throw new InvalidAttributeException("Price cannot be negative");
        }
        this.price = price;
    }

    /**
     * Assigns the identifier of the product.
     *
     * @param id the product identifier
     * @throws InvalidAttributeException if the identifier is negative
     */
    public void setId(String id) {
        if (Integer.parseInt(id) < 0) {
            throw new InvalidAttributeException("Id cannot be negative");
        }
        this.id = id;
    }

    /**
     * Returns a string representation of the product.
     *
     * @return a textual representation of the product
     */
    @Override
    public String toString() {
        return "{class:Product, id:" + this.getIdAsInt() + ", name:'" + this.getName() +
                "', price:" + this.getPrice() + "}";
    }

    /**
     * Returns the unit price of the product in the context of a ticket item.
     * <p>
     * For standard products, the unit price is equal to the base price.
     * Subclasses may override this method to apply dynamic pricing logic.
     * </p>
     *
     * @param context the ticket item context
     * @return the unit price
     */
    @Override
    public double getUnitPrice(TicketItem context) {
        return this.price;
    }
}