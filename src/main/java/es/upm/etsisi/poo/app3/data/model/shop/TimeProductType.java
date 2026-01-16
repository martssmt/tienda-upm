package es.upm.etsisi.poo.app3.data.model.shop;

/**
 * The {@code TimeProductType} enumeration defines different types of
 * time-based products available in the store system.
 * <p>
 * Each type specifies a planning time, expressed in hours, which represents
 * the minimum required time to organize or prepare the corresponding product.
 * </p>
 *
 * <p>
 * This enumeration is typically used to enforce business rules related to
 * scheduling, availability, or validation of time-dependent products.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * TimeProductType type = TimeProductType.FOOD;
 * int hours = type.getPlanningHours();
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 */
public enum TimeProductType {

    /**
     * Food-related time product, requiring 72 hours of planning.
     */
    FOOD(72),

    /**
     * Meeting-related time product, requiring 12 hours of planning.
     */
    MEETING(12);

    /**
     * Planning time required for the product type, expressed in hours.
     */
    private final Integer planningHours;

    /**
     * Creates a new time product type with the specified planning hours.
     *
     * @param planningHours the required planning time in hours
     */
    TimeProductType(Integer planningHours) {
        this.planningHours = planningHours;
    }

    /**
     * Returns the required planning time for the product type.
     *
     * @return the planning time in hours
     */
    public Integer getPlanningHours() {
        return this.planningHours;
    }
}
