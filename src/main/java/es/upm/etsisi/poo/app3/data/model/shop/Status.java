package es.upm.etsisi.poo.app3.data.model.shop;

/**
 * The {@code Status} enumeration defines the possible lifecycle states
 * of a ticket in the store system.
 * <p>
 * Each status represents a specific phase in the ticket lifecycle and is
 * used to control which operations are allowed at a given moment.
 * </p>
 *
 * <p>
 * Status values are typically used to validate actions such as adding or
 * removing items, printing tickets, or closing them.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * if (ticket.getStatus() == Status.OPEN) {
 *     // allow modifications
 * }
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 */
public enum Status {

    /**
     * Ticket has been created and can be modified.
     */
    OPEN,

    /**
     * Ticket contains no items.
     */
    EMPTY,

    /**
     * Ticket has been closed and can no longer be modified.
     */
    CLOSED
}
