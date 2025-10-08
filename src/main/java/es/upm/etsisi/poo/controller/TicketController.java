package es.upm.etsisi.poo.controller;

import es.upm.etsisi.poo.model.Catalog;
import es.upm.etsisi.poo.model.Product;
import es.upm.etsisi.poo.model.Ticket;
import es.upm.etsisi.poo.view.ConsoleView;

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
