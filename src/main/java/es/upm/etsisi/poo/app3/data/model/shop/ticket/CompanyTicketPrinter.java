package es.upm.etsisi.poo.app3.data.model.shop.ticket;

import es.upm.etsisi.poo.app3.data.model.shop.products.ServiceProduct;

public class CompanyTicketPrinter implements TicketPrintingStrategy {

    @Override
    public String format(Ticket ticket) {
        StringBuilder sb = new StringBuilder("Ticket : " + ticket.getName() + "\n");

        // 1. Sección de Servicios (sin precios)
        boolean hasServices = ticket.getItemList().stream()
                .anyMatch(item -> item.getPurchasable() instanceof ServiceProduct);

        if (hasServices) {
            sb.append("Services Included: \n");
            ticket.getItemList().stream()
                    .filter(item -> item.getPurchasable() instanceof ServiceProduct)
                    .forEach(item -> sb.append("  ").append(item.getPurchasable().toString()).append("\n"));
        }

        // 2. Sección de Productos (Si es combinado o solo productos)
        boolean hasProducts = ticket.getItemList().stream()
                .anyMatch(item -> !(item.getPurchasable() instanceof ServiceProduct));

        if (hasProducts) {
            sb.append("Product Included\n");
            ticket.getItemList().stream()
                    .filter(item -> !(item.getPurchasable() instanceof ServiceProduct))
                    .forEach(item -> {
                        sb.append("  ").append(item.getPurchasable().toString());
                        // Nota: Aquí podrías añadir lógica de descuento por categoría si aplicara
                        sb.append("\n");
                    });

            // Totales específicos de empresa
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
