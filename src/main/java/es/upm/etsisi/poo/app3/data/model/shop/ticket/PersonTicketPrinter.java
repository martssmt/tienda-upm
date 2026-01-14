package es.upm.etsisi.poo.app3.data.model.shop.ticket;

public class PersonTicketPrinter implements TicketPrintingStrategy {

    @Override
    public String format(Ticket ticket) {
        StringBuilder sb = new StringBuilder("Ticket : " + ticket.getName() + "\n");

        for (TicketItem item : ticket.getItemList()) {
            for (int i = 0; i < item.getQuantity(); i++) {
                sb.append("  ").append(item.getPurchasable().toString());
                if (item.getDiscount() > 0) {
                    sb.append(" **discount -").append(Math.round(item.getDiscount() * 100.0) / 100.0);
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
