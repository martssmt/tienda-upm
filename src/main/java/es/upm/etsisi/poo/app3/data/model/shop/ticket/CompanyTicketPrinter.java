package es.upm.etsisi.poo.app3.data.model.shop.ticket;

import es.upm.etsisi.poo.app3.data.model.shop.products.ServiceProduct;

/**
 * The {@code CompanyTicketPrinter} class implements a printing strategy for
 * tickets belonging to company clients.
 * <p>
 * This strategy supports both service-only tickets and combined tickets
 * (products + services). Services are always printed without prices, as their
 * final cost is calculated a posteriori (e.g., through invoicing).
 * </p>
 *
 * <p>
 * When products are present, totals are displayed and company-specific discounts
 * derived from contracted services are reflected in the final amounts.
 * </p>
 *
 * <p>
 * This class is part of the Strategy design pattern and is intended to be
 * injected into {@link Ticket} (directly or via lifecycle hooks such as {@code @PostLoad})
 * to customize formatting without changing ticket business logic.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * TicketPrintingStrategy printer = new CompanyTicketPrinter();
 * String output = printer.format(ticket);
 * System.out.println(output);
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 * @see TicketPrintingStrategy
 * @see Ticket
 * @see ServiceProduct
 */
public class CompanyTicketPrinter implements TicketPrintingStrategy {

    /**
     * Formats a ticket for a company client.
     * <p>
     * The output is divided into:
     * <ul>
     *   <li><b>Services section:</b> printed without prices.</li>
     *   <li><b>Products section:</b> printed normally if present, including totals and discounts.</li>
     * </ul>
     * If the ticket contains only services, totals are not printed since they are not
     * meaningful at ticket creation time.
     * </p>
     *
     * @param ticket the ticket to be formatted
     * @return a formatted string representation of the ticket
     */
    @Override
    public String format(Ticket ticket) {
        StringBuilder sb = new StringBuilder("Ticket : " + ticket.getName() + "\n");

        // 1. Services section (no prices)
        boolean hasServices = ticket.getItemList().stream()
                .anyMatch(item -> item.getPurchasable() instanceof ServiceProduct);

        if (hasServices) {
            sb.append("Services Included: \n");
            ticket.getItemList().stream()
                    .filter(item -> item.getPurchasable() instanceof ServiceProduct)
                    .forEach(item -> sb.append("  ").append(item.getPurchasable().toString()).append("\n"));
        }

        // 2. Products section (combined or product part of combined)
        boolean hasProducts = ticket.getItemList().stream()
                .anyMatch(item -> !(item.getPurchasable() instanceof ServiceProduct));

        if (hasProducts) {
            sb.append("Product Included\n");
            ticket.getItemList().stream()
                    .filter(item -> !(item.getPurchasable() instanceof ServiceProduct))
                    .forEach(item -> {
                        sb.append("  ").append(item.getPurchasable().toString());
                        sb.append("\n");
                    });

            // Company-specific totals
            double totalPrice = ticket.calculateTotalPrice();
            double serviceDiscount = ticket.calculateServiceDiscount();
            double totalDiscount = ticket.calculateTotalDiscount();

            sb.append("  Total price: ").append(totalPrice).append("\n");
            if (serviceDiscount > 0) {
                sb.append("  Extra Discount from services:").append(serviceDiscount)
                        .append(" **discount -").append(serviceDiscount).append("\n");
            }
            sb.append("  Total discount: ").append(totalDiscount).append("\n");
            sb.append("  Final Price: ").append(ticket.calculateFinalPrice());
        }

        return sb.toString().trim();
    }
}