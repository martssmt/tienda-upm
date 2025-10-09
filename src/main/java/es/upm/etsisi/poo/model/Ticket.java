package es.upm.etsisi.poo.model;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code Ticket} class represents a shopping receipt containing multiple
 * {@link TicketItem} objects. It allows adding and removing products,
 * calculating discounts, and displaying the total, discount, and final price
 * of all purchased items.
 * <p>
 * Each ticket can hold a maximum number of products (defined by
 * {@code MAX_PRODUCTS}). If this limit is exceeded, an exception is thrown.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * Product book = new Product(1, "Java Programming", Category.BOOK, 25.99);
 * ticket.addProduct(book, 2);
 * System.out.println(ticket);
 * }</pre>
 *
 * @author Jiling
 * @version 1.0
 * @see TicketItem
 * @see Product
 * @see Category
 */

public class Ticket {

    /** The list of items included in this ticket. */
    private final LinkedList<TicketItem> itemList;

    /** The maximum number of total products allowed per ticket. */
    private static final int MAX_PRODUCTS = 100;

    /** The current total number of products included in this ticket. */
    private int numberOfProducts;

    /**
     * Constructs an empty {@code Ticket} with no products.
     */
    public Ticket() {
        this.itemList = new LinkedList<>();
        this.numberOfProducts = 0;
    }

    /**
     * Adds a product to the ticket with the specified quantity.
     * <p>
     * If the product already exists in the ticket, its quantity is increased.
     * The method automatically sorts the item list after the addition.
     * </p>
     *
     * @param product  the product to be added
     * @param quantity the number of units to add
     * @throws NullPointerException if the {@code product} is {@code null}
     * @throws IllegalArgumentException if the quantity is less than or equal to zero,
     *                                  or if the ticket exceeds the {@code MAX_PRODUCTS} limit
     */
    public void addProduct(Product product, int quantity)
            throws IllegalArgumentException, NullPointerException {

        if (product == null) {
            throw new NullPointerException("Product cannot be null");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity cannot be negative or zero");
        }

        if (this.numberOfProducts + quantity >= MAX_PRODUCTS) {
            throw new IllegalArgumentException("The number of products cannot be greater than " + MAX_PRODUCTS);
        }

        boolean itemFound = false;
        Iterator<TicketItem> iterator = this.itemList.iterator();
        while (iterator.hasNext() && !itemFound) {
            TicketItem item = iterator.next();
            if (item.getProduct().equals(product)) {
                itemFound = true;
                item.setQuantity(item.getQuantity() + quantity);
            }
        }

        if (!itemFound) {
            TicketItem newItem = new TicketItem(product, quantity, product.getCategory().getDiscount());
            this.itemList.add(newItem);
        }

        this.numberOfProducts += quantity;
        this.itemList.sort(null);
    }

    /**
     * Removes a product from the ticket.
     *
     * @param product the product to remove from the ticket
     * @throws IllegalArgumentException if the specified product does not exist in the ticket
     */
    public void removeProduct(Product product) throws IllegalArgumentException {
        boolean itemFound = false;
        Iterator<TicketItem> iterator = this.itemList.iterator();
        while (iterator.hasNext() && !itemFound) {
            TicketItem item = iterator.next();
            if (item.getProduct().equals(product)) {
                itemFound = true;
                this.numberOfProducts -= item.getQuantity();
                this.itemList.remove(item);
            }
        }
        if (!itemFound) {
            throw new IllegalArgumentException("Product is not included in the ticket");
        }
    }

    /**
     * Clears all products from the ticket, resetting its state.
     */
    public void clear() {
        this.itemList.clear();
        this.numberOfProducts = 0;
    }

    /**
     * Calculates the total amount of discount applied to the ticket.
     * <p>
     * A discount is applied to all products belonging to categories with more than
     * one item in the ticket.
     * </p>
     *
     * @return the total discount applied to all products in the ticket
     */
    private double calculateTotalDiscount() {
        double result = 0;

        Map<Category, Integer> quantitiesEachCategory = new HashMap<>();
        for (TicketItem item : this.itemList) {
            Category category = item.getProduct().getCategory();
            int currentQuantity = quantitiesEachCategory.getOrDefault(category, 0);
            quantitiesEachCategory.put(category, currentQuantity + item.getQuantity());
        }

        for (TicketItem item : this.itemList) {
            Category category = item.getProduct().getCategory();
            int totalEachCategory = quantitiesEachCategory.get(category);
            if (totalEachCategory > 1) {
                result += item.getDiscount();
            }
        }
        return result;
    }

    /**
     * Calculates the total price of all products before discounts.
     *
     * @return the total price without discounts
     */
    private double calculateTotalPrice() {
        double result = 0;
        for (TicketItem item : this.itemList) {
            result += item.getSubtotal();
        }
        return result;
    }

    /**
     * Calculates the final price after applying discounts.
     *
     * @return the total amount to pay after all discounts
     */
    private double calculateFinalPrice() {
        return this.calculateTotalPrice() - this.calculateTotalDiscount();
    }

    /**
     * Returns a formatted string representation of the ticket.
     * <p>
     * Each product line displays its information, applicable discount, and
     * the total, discount, and final prices are printed at the end.
     * </p>
     *
     * @return a formatted string containing all ticket details
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        Map<Category, Integer> quantitiesEachCategory = new HashMap<>();
        for (TicketItem item : this.itemList) {
            Category category = item.getProduct().getCategory();
            int currentQuantity = quantitiesEachCategory.getOrDefault(category, 0);
            quantitiesEachCategory.put(category, currentQuantity + item.getQuantity());
        }

        for (TicketItem item : this.itemList) {
            double discountEachProduct = item.getProduct().getPrice() * item.getProduct().getCategory().getDiscount();
            Category category = item.getProduct().getCategory();

            for (int i = 0; i < item.getQuantity(); i++) {
                result.append(item.getProduct().toString());
                if (quantitiesEachCategory.get(category) > 1 && discountEachProduct > 0) {
                    result.append("  **discount -").append(discountEachProduct);
                }
                result.append("\n");
            }
        }

        result.append("Total price: ").append(this.calculateTotalPrice()).append("\n");
        result.append("Total discount: ").append(this.calculateTotalDiscount()).append("\n");
        result.append("Final Price: ").append(this.calculateFinalPrice());

        return result.toString();
    }
}