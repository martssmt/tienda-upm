package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.shop.Category;
import jakarta.persistence.*;

/**
 * The {@code BasicProduct} class represents a standard purchasable product
 * available in the store system.
 * <p>
 * A basic product is characterized by a name, a unit price, and a
 * {@link Category}, which determines the discount rate applied to the product.
 * </p>
 *
 * <p>
 * This class extends {@link Product} and provides category-based behavior
 * while relying on the parent class for pricing and identification logic.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * BasicProduct product = new BasicProduct("Notebook", Category.STATIONERY, 2.50);
 * Category category = product.getCategory();
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 * @see Product
 * @see Category
 */
@jakarta.persistence.Entity
@Table(name = "basic_products")
public class BasicProduct extends Product {

    /**
     * Category associated with the product, used to determine discount rates.
     */
    @Enumerated(EnumType.STRING)
    private Category category;

    /**
     * Protected no-argument constructor required by JPA.
     */
    protected BasicProduct() {
        super();
    }

    /**
     * Creates a new basic product with the given name, category, and price.
     *
     * @param name     the product name
     * @param category the product category
     * @param price    the unit price of the product
     */
    public BasicProduct(String name, Category category, Double price) {
        super(name, price);
        this.category = category;
    }

    /**
     * Copy constructor.
     * <p>
     * Creates a new {@code BasicProduct} instance by copying the state of
     * an existing one.
     * </p>
     *
     * @param original the original product to copy
     */
    public BasicProduct(BasicProduct original) {
        super(original);
        this.category = original.getCategory();
    }

    /**
     * Returns the category of the product.
     *
     * @return the product category
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * Sets the category of the product.
     *
     * @param category the new product category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Returns a string representation of the basic product.
     *
     * @return a textual representation of the product
     */
    @Override
    public String toString() {
        return "{class:Product, id:" + this.getIdAsInt() + ", name:'" + this.getName() +
                "', category:" + this.category + ", price:" + this.getPrice() + "}";
    }

    /**
     * Validates the availability of the product.
     * <p>
     * Basic products do not impose additional availability constraints,
     * so this method performs no action.
     * </p>
     */
    @Override
    public void validateAvailability() {}
}