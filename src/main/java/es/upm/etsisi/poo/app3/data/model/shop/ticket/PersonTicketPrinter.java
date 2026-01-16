package es.upm.etsisi.poo.app3.data.model.shop.ticket;

import es.upm.etsisi.poo.app3.data.model.shop.Category;
import es.upm.etsisi.poo.app3.data.model.shop.products.BasicProduct;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code PersonTicketPrinter} class implements a printing strategy for
 * tickets belonging to personal (non-company) clients.
 * <p>
 * This strategy formats tickets that contain only products, displaying
 * individual item prices, applied discounts, and the final calculated totals.
 * </p>
 *
 * <p>
 * The class is part of a Strategy design pattern, where different
 * {@link TicketPrintingStrategy} implementations are injected to customize
 * ticket visualization depending on the client and ticket type.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * TicketPrintingStrategy printer = new PersonTicketPrinter();
 * String output = printer.format(ticket);
 * System.out.println(output);
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 * @see TicketPrintingStrategy
 * @see Ticket
 */
public class PersonTicketPrinter implements TicketPrintingStrategy {

    /**
     * Formats a ticket for a personal client.
     * <p>
     * Each product is printed individually according to its quantity.
     * Discounts applied to items are explicitly shown, along with the total
     * price, total discount, and final price of the ticket.
     * </p>
     *
     * @param ticket the ticket to be formatted
     * @return a formatted string representation of the ticket
     */
    @Override
    public String format(Ticket ticket) {
        StringBuilder sb = new StringBuilder("Ticket : " + ticket.getName() + "\n");

        Map<Category, Integer> categoryCounts = new HashMap<>();
        for (TicketItem item : ticket.getItemList()) {
            if (item.getPurchasable() instanceof BasicProduct bp) {
                Category cat = bp.getCategory();
                categoryCounts.put(cat, categoryCounts.getOrDefault(cat, 0) + item.getQuantity());
            }
        }

        for (TicketItem item : ticket.getItemList()) {
            double unitDiscount = item.getUnitDiscount();
            boolean hasDiscount = item.getPurchasable() instanceof BasicProduct bp && categoryCounts.get(bp.getCategory()) >= 2;
            for (int i = 0; i < item.getQuantity(); i++) {
                sb.append("  ").append(item);
                if (hasDiscount && unitDiscount > 0) {
                    sb.append(" **discount -").append(unitDiscount);
                }
                sb.append("\n");
            }
        }

        sb.append("  Total price: ").append(ticket.calculateTotalPrice()).append("\n");
        sb.append("  Total discount: ").append(ticket.calculateTotalDiscount()).append("\n");
        sb.append("  Final Price: ").append(ticket.calculateFinalPrice());

        return sb.toString();
    }
}