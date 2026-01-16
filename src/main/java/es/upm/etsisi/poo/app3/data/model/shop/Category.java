package es.upm.etsisi.poo.app3.data.model.shop;

/**
 * The {@code Category} enumeration defines the different product categories
 * available in the store system.
 * <p>
 * Each category is associated with a discount percentage that is applied
 * to products belonging to that category during ticket calculation.
 * </p>
 *
 * <p>
 * Category discounts are used as part of the pricing and billing logic and
 * may be combined with additional rules depending on the ticket type.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * Category category = Category.BOOK;
 * double discount = category.getDiscount();
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 */
public enum Category {

    /**
     * Merchandise products with no discount applied.
     */
    MERCH(0.0),

    /**
     * Stationery products with a 5% discount.
     */
    STATIONERY(0.05),

    /**
     * Clothing products with a 7% discount.
     */
    CLOTHES(0.07),

    /**
     * Book products with a 10% discount.
     */
    BOOK(0.10),

    /**
     * Electronic products with a 3% discount.
     */
    ELECTRONICS(0.03);

    /**
     * Discount percentage associated with the category.
     */
    private final double discount;

    /**
     * Creates a new category with the specified discount percentage.
     *
     * @param discount the discount percentage associated with the category
     */
    Category(double discount) {
        this.discount = discount;
    }

    /**
     * Returns the discount percentage associated with the category.
     *
     * @return the discount percentage
     */
    public double getDiscount() {
        return this.discount;
    }
}
