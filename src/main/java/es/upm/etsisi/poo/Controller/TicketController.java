package es.upm.etsisi.poo.Controller;

import es.upm.etsisi.poo.Model.Catalog;
import es.upm.etsisi.poo.Model.Product;
import es.upm.etsisi.poo.Model.Ticket;
import es.upm.etsisi.poo.View.ConsoleView;

public class TicketController {

    private Ticket ticket;
    private Catalog catalog;

    public TicketController(Catalog catalog) {
        this.catalog = catalog;
        this.ticket = new Ticket();
    }

    public void handleNew() {
        this.ticket.clear();
        ConsoleView.showMessage("ticket new: ok");
    }

    public void handleAdd(int id, int quantity) {
        Product product = this.catalog.getProduct(id);
        this.ticket.addProduct(product, quantity);
        ConsoleView.showTicket(this.ticket);
        ConsoleView.showMessage("ticket add: ok");
    }

    public void handleRemove(int id) {
        this.ticket.removeProduct(id);
        ConsoleView.showTicket(this.ticket);
        ConsoleView.showMessage("ticket remove: ok");
    }

    public void handlePrint() {
        ConsoleView.showTicket(this.ticket);
        ConsoleView.showMessage("ticket print: ok");
    }

}
