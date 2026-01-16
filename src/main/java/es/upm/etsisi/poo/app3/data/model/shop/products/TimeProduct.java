package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.TimeProductType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The {@code TimeProduct} class represents a time-dependent product in the store system.
 * <p>
 * A time product is a {@link Product} that can only be purchased if it satisfies
 * scheduling constraints. Each instance has an opening/event date and a minimum
 * planning time (in hours) that must be respected when adding or validating the product.
 * </p>
 *
 * <p>
 * The planning time is derived from the {@link TimeProductType} and is used to ensure
 * that the event date is sufficiently far in the future. Additionally, the product
 * enforces a global maximum capacity constraint ({@code MAX_PEOPLE_GLOBAL}).
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * TimeProduct p = new TimeProduct(
 *     "Team meeting", TimeProductType.MEETING, 15.0,
 *     LocalDate.now().plusDays(2), 20
 * );
 * p.validateAvailability();
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 * @see Product
 * @see TimeProductType
 */
@jakarta.persistence.Entity
@Table(name = "time_products")
public class TimeProduct extends Product {

    /**
     * Date of the event/opening associated with this time product.
     */
    @Column(name = "open_date")
    private LocalDate openDate;

    /**
     * Global maximum number of people allowed for any time product.
     */
    private static final Integer MAX_PEOPLE_GLOBAL = 100;

    /**
     * Maximum number of people allowed for this specific time product instance.
     */
    @Column(name = "max_people")
    private Integer maxPeople;

    /**
     * Type of time product, which determines planning constraints.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private TimeProductType type;

    /**
     * Required planning time in hours before the event can take place.
     */
    @Column(name = "planning_hours")
    private Integer planningHours;

    /**
     * Protected no-argument constructor required by JPA.
     */
    protected TimeProduct() {
        super();
    }

    /**
     * Creates a new time product with the given attributes.
     * <p>
     * This constructor enforces:
     * <ul>
     *   <li>A maximum capacity limit ({@code maxPeople} must not exceed {@link #MAX_PEOPLE_GLOBAL}).</li>
     *   <li>A minimum planning time constraint based on {@link TimeProductType#getPlanningHours()}.</li>
     * </ul>
     * If any rule is violated, an {@link InvalidAttributeException} is thrown.
     * </p>
     *
     * @param name      the product name
     * @param type      the time product type
     * @param price     the base unit price
     * @param openDate  the event/opening date
     * @param maxPeople the maximum number of people allowed for this event
     * @throws InvalidAttributeException if capacity exceeds the global maximum or the
     *                                   event date does not satisfy the planning constraint
     */
    public TimeProduct(String name, TimeProductType type, Double price, LocalDate openDate, Integer maxPeople) {
        super(name, price);
        this.type = type;
        this.openDate = openDate;

        if (maxPeople > MAX_PEOPLE_GLOBAL) {
            throw new InvalidAttributeException("Error adding product");
        }
        this.maxPeople = maxPeople;

        this.planningHours = type.getPlanningHours();
        LocalDateTime minAllowedDate = LocalDateTime.now().plusHours(this.planningHours);
        LocalDateTime openingDateTime = this.openDate.atStartOfDay();
        if (openingDateTime.isBefore(minAllowedDate)) {
            throw new InvalidAttributeException("Error adding product");
        }
    }

    /**
     * Copy constructor.
     * <p>
     * Creates a new {@code TimeProduct} instance by copying the state of an existing one.
     * </p>
     *
     * @param original the original time product to copy
     */
    public TimeProduct(TimeProduct original) {
        super(original);
        this.type = original.type;
        this.openDate = original.openDate;
        this.maxPeople = original.maxPeople;
        this.planningHours = original.planningHours;
    }

    /**
     * Returns the event/opening date associated with the product.
     *
     * @return the opening date
     */
    public LocalDate getOpenDate() {
        return this.openDate;
    }

    /**
     * Returns the global maximum allowed number of people.
     *
     * @return the global maximum capacity
     */
    public static Integer getMaxPeopleGlobal() {
        return MAX_PEOPLE_GLOBAL;
    }

    /**
     * Returns the type of the time product.
     *
     * @return the time product type
     */
    public TimeProductType getType() {
        return this.type;
    }

    /**
     * Returns the required planning time in hours.
     *
     * @return the planning time in hours
     */
    public Integer getPlanningHours() {
        return this.planningHours;
    }

    /**
     * Returns the maximum number of people allowed for this specific event.
     *
     * @return the maximum people allowed
     */
    public Integer getMaxPeople() {
        return maxPeople;
    }

    /**
     * Returns a string representation of the time product.
     *
     * @return a textual representation of the time product
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        String productType;
        if (type == TimeProductType.MEETING) {
            productType = "Meeting";
        } else {
            productType = "Food";
        }

        stringBuilder.append("{class:").append(productType)
                .append(", id:").append(this.getIdAsInt())
                .append(", name:'").append(this.getName()).append("'")
                .append(", price:").append(this.getPrice())
                .append(", date of Event:");

        if (this.openDate == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(openDate);
        }

        stringBuilder.append(", max people allowed:");
        stringBuilder.append(MAX_PEOPLE_GLOBAL);

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    /**
     * Validates whether the time product is still available.
     * <p>
     * A time product is available only if its event date is not earlier than
     * the minimum allowed date computed as {@code now + planningHours}.
     * </p>
     *
     * @throws InvalidAttributeException if the product is considered expired
     */
    @Override
    public void validateAvailability() {
        LocalDateTime minDate = LocalDateTime.now().plusHours(this.planningHours);
        if (openDate.atStartOfDay().isBefore(minDate)) {
            throw new InvalidAttributeException("Product is already expired");
        }
    }
}