package es.upm.etsisi.poo.model;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Catalog {
    private static final int MAX_PRODUCTS = 200;
    private final Map<Integer, Product> productsList;

    public Catalog() {
        this.productsList = new HashMap<>();
    }

    public Product getProduct(int id) {
        return this.productsList.get(id);
    }

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Catalog:\n");
        for (Product product : this.productsList.values()) {
            sb.append("\t" + product + "\n");
        }
        return sb.toString();
    }
}