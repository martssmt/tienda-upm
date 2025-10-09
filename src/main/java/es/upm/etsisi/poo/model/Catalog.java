package es.upm.etsisi.poo.model;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * The {@code Catalog} class represents a collection of {@link Product} objects
 * that can be managed within the store application.
 * <p>
 * It provides methods to add, retrieve, remove, and update products,
 * while ensuring data integrity through internal validation and exception handling.
 * <p>
 * The catalog can contain up to a fixed number of products
 * (defined by {@code MAX_PRODUCTS}).
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * Catalog catalog = new Catalog();
 * Product book = new Product(1, "Java Programming", Category.BOOK, 25.99);
 * catalog.addProduct(book);
 * catalog.updateProduct(1, "price", "29.99");
 * System.out.println(catalog);
 * }</pre>
 *
 * @author Tomas
 * @version 1.0
 * @see Product
 * @see Category
 */

public class Catalog {

    /** The maximum number of products allowed in the catalog. */
    private static final int MAX_PRODUCTS = 200;

    /** Internal data structure to store products mapped by their ID. */
    private final Map<Integer, Product> productsList;

    /**
     * Constructs an empty {@code Catalog} with no products.
     */
    public Catalog() {
        this.productsList = new HashMap<>();
    }

    /**
     * Retrieves a product from the catalog by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return the {@link Product} with the given ID, or {@code null} if it does not exist
     */
    public Product getProduct(int id) {
        return this.productsList.get(id);
    }

    /**
     * Adds a new product to the catalog.
     * <p>
     * Validates that the product is not {@code null}, that the catalog has not
     * reached its maximum capacity, and that no other product with the same ID exists.
     * </p>
     *
     * @param product the product to be added
     * @throws IllegalArgumentException if the product is {@code null}
     * @throws IllegalStateException if the catalog is full or if a product with the same ID already exists
     */
    public void addProduct(Product product) throws IllegalArgumentException, IllegalStateException {
        if (product == null) {
            throw new IllegalArgumentException("The product cannot be null");
        }

        if (this.productsList.size() >= MAX_PRODUCTS) {
            throw new IllegalStateException("The catalog is full (max " + MAX_PRODUCTS + " products)");
        }

        if (this.productsList.containsKey(product.getId())) {
            throw new IllegalStateException("A product with ID " + product.getId() + " already exists");
        }

        this.productsList.put(product.getId(), product);
    }

    /**
     * Removes a product from the catalog using its ID.
     *
     * @param id the ID of the product to be removed
     * @throws IllegalArgumentException if the provided ID is less than or equal to zero
     * @throws NoSuchElementException if no product with the given ID exists in the catalog
     */
    public void removeProduct(int id)
            throws IllegalArgumentException, NoSuchElementException {
        if (id <= 0) {
            throw new IllegalArgumentException("The product ID must be positive");
        }

        if (!this.productsList.containsKey(id)) {
            throw new NoSuchElementException("No product found with ID " + id);
        }

        this.productsList.remove(id);
    }

    /**
     * Updates a field of an existing product in the catalog.
     * <p>
     * Supports updating the {@code name}, {@code category}, or {@code price} fields.
     * </p>
     *
     * @param id    the ID of the product to update
     * @param field the field to update ("name", "category", or "price")
     * @param value the new value to assign
     * @throws IllegalArgumentException if the provided field is invalid or if the new value is not valid
     * @throws NoSuchElementException if no product with the given ID exists
     * @throws NumberFormatException if the {@code price} value cannot be parsed as a valid number
     */
    public void updateProduct(int id, String field, String value)
            throws IllegalArgumentException, NoSuchElementException {

        Product product = this.productsList.get(id);
        if (product == null) {
            throw new NoSuchElementException("No product found with ID " + id);
        }

        switch (field.toLowerCase()) {
            case "name":
                if (value == null || value.isBlank()) {
                    throw new IllegalArgumentException("Name cannot be empty");
                }
                value = value.substring(1, value.length() - 1);
                product.setName(value);
                break;

            case "category":
                try {
                    Category category = Category.valueOf(value.toUpperCase());
                    product.setCategory(category);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid category: " + value);
                }
                break;

            case "price":
                try {
                    double newPrice = Double.parseDouble(value);
                    product.setPrice(newPrice);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Price must be a valid number");
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid field: " + field);
        }
    }

    /**
     * Returns a string representation of the catalog, listing all stored products.
     *
     * @return a formatted string containing all products in the catalog
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Catalog:\n");
        for (Product product : this.productsList.values()) {
            sb.append("\t").append(product).append("\n");
        }
        return sb.toString();
    }
}