package es.upm.etsisi.poo.model;

/**
 * The {@code Category} enumeration represents the different product categories
 * available in the store, each associated with a specific discount rate.
 * <p>
 * Each constant in this enumeration defines a category and its corresponding discount,
 * which can be used to calculate discounted prices or promotions for products.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * Category bookCategory = Category.BOOK;
 * double discount = bookCategory.getDiscount(); // 0.1 (10% discount)
 * }</pre>
 *
 * @author Tomas
 * @version 1.0
 */

public enum Category {

    /** Merchandise category. No discount applied. */
    MERCH(0),

    /** Stationery products category. 5% discount applied. */
    STATIONERY(0.05),

    /** Clothing category. 7% discount applied. */
    CLOTHES(0.07),

    /** Book category. 10% discount applied. */
    BOOK(0.1),

    /** Electronics category. 3% discount applied. */
    ELECTRONICS(0.03);

    /** The discount rate associated with the category. */
    private final double discount;

    /**
     * Constructs a {@code Category} constant with the specified discount rate.
     *
     * @param discount the discount percentage (as a decimal, e.g. 0.1 for 10%)
     */
    Category(double discount) {
        this.discount = discount;
    }

    /**
     * Returns the discount rate associated with this category.
     *
     * @return the discount rate as a decimal value (e.g. {@code 0.1} = 10%)
     */
    public double getDiscount() {
        return this.discount;
    }
}
