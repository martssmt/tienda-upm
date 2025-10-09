package es.upm.etsisi.poo.model;

/**
 * The {@code TicketItem} class represents a single line item within a {@link Ticket}.
 * <p>
 * Each {@code TicketItem} stores a reference to a {@link Product}, the quantity purchased,
 * and the discount rate applied to that product.
 * It provides methods to calculate the subtotal (price × quantity) and
 * the total discount amount for the product line.
 * </p>
 *
 * <p>
 * This class implements {@link Comparable}, allowing items to be sorted alphabetically
 * by product name within a ticket.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * Product book = new Product(1, "Java Programming", Category.BOOK, 25.99);
 * TicketItem item = new TicketItem(book, 2, book.getCategory().getDiscount());
 * System.out.println(item.getSubtotal()); // 51.98
 * System.out.println(item.getDiscount()); // 5.198 (10% discount)
 * }</pre>
 *
 * @author Jiling
 * @version 1.0
 * @see Ticket
 * @see Product
 * @see Category
 */

public class TicketItem implements Comparable<TicketItem> {

    /** The product associated with this ticket item. */
    private final Product product;

    /** The number of units of the product purchased. */
    private int quantity;

    /** The discount rate applied to this item (as a decimal value, e.g. 0.1 = 10%). */
    private final double discountApplied;

    /**
     * Constructs a new {@code TicketItem} with the specified product, quantity, and discount rate.
     *
     * @param product         the product included in this ticket item
     * @param quantity        the quantity of the product purchased
     * @param discountApplied the discount rate applied to the product (as a decimal value)
     */
    public TicketItem(Product product, int quantity, double discountApplied) {
        this.product = product;
        this.quantity = quantity;
        this.discountApplied = discountApplied;
    }

    /**
     * Calculates the subtotal for this item (price multiplied by quantity).
     *
     * @return the subtotal price of this item before discounts
     */
    public double getSubtotal() {
        return this.product.getPrice() * this.quantity;
    }

    /**
     * Calculates the total discount applied to this item.
     *
     * @return the total discount amount (subtotal × discount rate)
     */
    public double getDiscount() {
        return this.discountApplied * this.getSubtotal();
    }

    /**
     * Returns the product associated with this ticket item.
     *
     * @return the {@link Product} for this item
     */
    public Product getProduct() {
        return this.product;
    }

    /**
     * Returns the quantity of products in this item.
     *
     * @return the quantity of units purchased
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Updates the quantity of products in this ticket item.
     *
     * @param quantity the new quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Compares this {@code TicketItem} to another based on the name of their associated products.
     * <p>
     * This comparison enables sorting of items alphabetically by product name.
     * </p>
     *
     * @param ticketItem the other {@code TicketItem} to compare with
     * @return a negative integer, zero, or a positive integer if this item's product name
     *         is lexicographically less than, equal to, or greater than the other item's name
     */
    @Override
    public int compareTo(TicketItem ticketItem) {
        return this.product.getName().compareTo(ticketItem.getProduct().getName());
    }
}