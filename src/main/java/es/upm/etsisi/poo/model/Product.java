package es.upm.etsisi.poo.model;

/**
 * The {@code Product} class represents an item available in the store catalog.
 * <p>
 * Each product has a unique identifier, a name, a category, and a price.
 * It provides validation to ensure that products are always created and updated
 * with valid data (e.g., non-empty names and positive prices).
 * <p>
 * This class is part of the model layer of the application and is used by the
 * {@code Catalog} class to manage collections of products.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * Product book = new Product(1, "Java Programming", Category.BOOK, 29.99);
 * }</pre>
 *
 * @author Tomas
 * @version 1.0
 */

public class Product {
    /** The unique identifier for the product. */
    private final int id;
    /** The name or title of the product. */
    private String name;
    /** The category to which the product belongs. */
    private Category category;
    /** The price of the product. Must be greater than 0. */
    private double price;

    /**
     * Constructs a new {@code Product} instance with the specified attributes.
     *
     * @param id        the unique identifier of the product
     * @param name      the name of the product (cannot be null or empty)
     * @param category  the category of the product
     * @param price     the price of the product (must be greater than 0)
     * @throws IllegalArgumentException if {@code name} is null, empty, longer than 100 characters,
     *                                  or if {@code price} is less than or equal to 0
     */
    public Product(int id, String name, Category category, double price)
            throws IllegalArgumentException {
        if (name == null || name.isEmpty() || name.length() > 100) {
            throw new IllegalArgumentException("The name field cannot be empty or exceed 100 characters");
        }
        if (price <= 0) throw new IllegalArgumentException("The price field must be greater than 0");

        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    /**
     * Updates the name of the product.
     *
     * @param name the new name for the product
     * @throws IllegalArgumentException if {@code name} is null, empty, or longer than 100 characters
     */
    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty() || name.length() > 100) {
            throw new IllegalArgumentException("The name field cannot be empty or exceed 100 characters");
        }
        this.name = name;
    }

    /**
     * Updates the category of the product.
     *
     * @param category the new category to assign
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Updates the price of the product.
     *
     * @param price the new price for the product
     * @throws IllegalArgumentException if {@code price} is less than or equal to 0
     */
    public void setPrice(double price) throws IllegalArgumentException {
        if (price <= 0) throw new IllegalArgumentException("The price field must be greater than 0");
        this.price = price;
    }

    /**
     * Returns the unique identifier of the product.
     *
     * @return the product ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the name of the product.
     *
     * @return the product name
     */
    public String getName() {
        return this.name;
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
     * Returns the price of the product.
     *
     * @return the product price
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * Compares this product to another product based on their IDs.
     * Two products are considered equal if they share the same ID.
     *
     * @param product the product to compare with
     * @return {@code true} if both products have the same ID, {@code false} otherwise
     */
    private boolean equals(Product product) {
        return this.id == product.id;
    }

    /**
     * Checks whether this product is equal to another object.
     * Two products are equal if they have the same ID.
     *
     * @param obj the object to compare with
     * @return {@code true} if the given object is a {@code Product} with the same ID,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            return this.equals((Product) obj);
        } else {
            return false;
        }
    }

    /**
     * Returns a hash code value for the product, based on its ID.
     *
     * @return the hash code for this product
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    /**
     * Returns a string representation of the product in a JSON-like format.
     *
     * @return a formatted string describing this product
     */
    @Override
    public String toString() {
        return "{class:Product, id:" + this.id + ", name:'" + this.name + "'," +
                " category:" + this.category.name() + ", price:" + this.price + "}";
    }
}