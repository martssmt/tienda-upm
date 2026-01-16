package es.upm.etsisi.poo.app3.data.model.shop;

/**
 * The {@code TicketType} enumeration defines the different types of tickets
 * supported by the store system.
 * <p>
 * Each ticket type determines the kind of items that can be associated with
 * the ticket and the business rules applied during its lifecycle.
 * </p>
 *
 * <p>
 * Ticket types are especially relevant when validating ticket creation,
 * product and service inclusion, and ticket closing logic.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * TicketType type = TicketType.COMBINED;
 * if (type == TicketType.SERVICE) {
 *     // service-only ticket rules
 * }
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 */
public enum TicketType {

    /**
     * Ticket that contains only products.
     */
    PRODUCT,

    /**
     * Ticket that contains only services.
     */
    SERVICE,

    /**
     * Ticket that contains both products and services.
     */
    COMBINED
}
