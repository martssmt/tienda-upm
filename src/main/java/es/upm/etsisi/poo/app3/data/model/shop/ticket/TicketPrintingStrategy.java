package es.upm.etsisi.poo.app3.data.model.shop.ticket;

/**
 * The {@code TicketPrintingStrategy} interface defines a strategy for
 * formatting tickets into a textual representation.
 * <p>
 * This interface is part of the Strategy design pattern, allowing different
 * printing behaviors to be injected into a {@link Ticket} depending on
 * the client type and ticket configuration.
 * </p>
 *
 * <p>
 * Concrete implementations encapsulate distinct formatting rules, such as
 * personal tickets, company tickets, or combined tickets, without modifying
 * the {@link Ticket} class itself.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * TicketPrintingStrategy printer = new PersonTicketPrinter();
 * String output = printer.format(ticket);
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 * @see Ticket
 */
public interface TicketPrintingStrategy {

    /**
     * Formats the given ticket according to a specific printing strategy.
     *
     * @param ticket the ticket to be formatted
     * @return a formatted string representation of the ticket
     */
    String format(Ticket ticket);
}
