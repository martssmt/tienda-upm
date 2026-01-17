package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.Category;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.TicketItem;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

/**
 * The {@code CustomProduct} class represents a customizable product in the store system.
 * <p>
 * A custom product extends {@link BasicProduct} by allowing personalization through
 * user-defined text fragments. The final unit price depends on the number of
 * custom texts applied to the product.
 * </p>
 *
 * <p>
 * Each customization increases the base price by a fixed percentage, which is
 * dynamically calculated at ticket evaluation time using the {@link TicketItem}
 * context.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * CustomProduct product = new CustomProduct("Mug", Category.MERCH, 10.0, 3);
 * double price = product.getUnitPrice(ticketItem);
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 * @see BasicProduct
 * @see TicketItem
 */
@jakarta.persistence.Entity
@Table(name = "custom_products")
public class CustomProduct extends BasicProduct {

    /**
     * Base price of the product before personalization.
     */
    @Column(name = "original_price")
    private Double originalPrice;

    /**
     * Maximum number of custom text fragments allowed for this product.
     */
    @Column(name = "number_texts")
    private Integer numberTexts;

    /**
     * Protected no-argument constructor required by JPA.
     */
    protected CustomProduct() {
        super();
    }

    /**
     * Creates a new custom product with the given attributes.
     *
     * @param name          the product name
     * @param category      the product category
     * @param originalPrice the base price before personalization
     * @param numberTexts   the maximum number of custom texts allowed
     */
    public CustomProduct(String name, Category category, Double originalPrice, Integer numberTexts) {
        super(name, category, originalPrice);
        this.originalPrice = originalPrice;
        this.numberTexts = numberTexts;
    }

    /**
     * Copy constructor.
     * <p>
     * Creates a new {@code CustomProduct} by copying the state of an existing one.
     * </p>
     *
     * @param original the original product to copy
     */
    public CustomProduct(CustomProduct original) {
        super(original);
        this.originalPrice = original.originalPrice;
        this.numberTexts = original.numberTexts;
    }

    /**
     * Returns the original base price of the product.
     *
     * @return the original price
     */
    public Double getOriginalPrice() {
        return this.originalPrice;
    }

    /**
     * Sets the original base price of the product.
     *
     * @param originalPrice the new base price
     * @throws InvalidAttributeException if the price is negative
     */
    public void setOriginalPrice(Double originalPrice) {
        if (originalPrice < 0) {
            throw new InvalidAttributeException("OriginalPrice cannot be negative");
        }
        this.originalPrice = originalPrice;
    }

    /**
     * Returns the maximum number of custom texts allowed for this product.
     *
     * @return the maximum number of custom texts
     */
    public Integer getNumberTexts() {
        return numberTexts;
    }

    /**
     * Sets the maximum number of custom texts allowed for this product.
     *
     * @param numberTexts the maximum number of custom texts
     * @throws InvalidAttributeException if the value is negative
     */
    public void setNumberTexts(Integer numberTexts) {
        if (numberTexts < 0) {
            throw new InvalidAttributeException("NumberTexts cannot be negative");
        }
        this.numberTexts = numberTexts;
    }

    /**
     * Calculates the unit price of the product in the context of a ticket item.
     * <p>
     * Each custom text increases the base price by 10%.
     * </p>
     *
     * @param context the ticket item context containing customization information
     * @return the calculated unit price
     */
    @Override
    public double getUnitPrice(TicketItem context) {
        return this.originalPrice * (1 + 0.1 * context.getCustomTextsSize());
    }

    /**
     * Returns a string representation of the custom product.
     *
     * @return a textual representation of the custom product
     */
    @Override
    public String toString() {
        return "{class:ProductPersonalized, id:" + this.id + ", name:'" + this.getName() + "', category:" +
                this.getCategory() + ", price:" + this.originalPrice + ", maxPersonal:" + this.numberTexts + "}";
    }
}