package es.upm.etsisi.poo.Controller;

import es.upm.etsisi.poo.Model.Catalog;
import es.upm.etsisi.poo.Model.Product;
import es.upm.etsisi.poo.Model.Ticket;
import es.upm.etsisi.poo.View.ConsoleView;

public class TicketController {

    private final Ticket ticket;
    private final Catalog catalog;

    public TicketController(Catalog catalog) {
        this.catalog = catalog;
        this.ticket = new Ticket();
    }

    public void handleNew() {
        this.ticket.clear();
        ConsoleView.showMessage("ticket new: ok");
    }

    public void handleAdd(int id, int quantity) throws IllegalStateException {
        Product product = this.catalog.getProduct(id);
        if (product == null) {
            throw new IllegalStateException("No product found with ID " + id + " in the catalog");
        }
        this.ticket.addProduct(product, quantity);
        ConsoleView.showTicket(this.ticket);
        ConsoleView.showMessage("ticket add: ok");
    }

    public void handleRemove(int id) throws IllegalStateException {
        Product product = catalog.getProduct(id);
        if (product == null) {
            throw new IllegalStateException("No product found with ID " + id + " in the catalog");
        }
        this.ticket.removeProduct(product);
        ConsoleView.showTicket(this.ticket);
        ConsoleView.showMessage("ticket remove: ok");
    }

    public void handlePrint() {
        ConsoleView.showTicket(this.ticket);
        ConsoleView.showMessage("ticket print: ok");
    }

}
