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
        if (product == null) {
            ConsoleView.showMessage("Product with id " + id + " does not exist in the catalog.");
            this.handlePrint();
            ConsoleView.showMessage("ticket add: error");
        } else {
            boolean productAdded = this.ticket.addProduct(product, quantity);
            this.handlePrint();
            if (productAdded) {
                ConsoleView.showMessage("ticket add: ok");
            } else {
                ConsoleView.showMessage("ticket add: error");
            }
        }
    }

    public void handleRemove(int id) {
        Product product = this.ticket.getProduct(id);
        if (product == null) {
            ConsoleView.showMessage("Product with id " + id + " does not exist in the ticket.");
            this.handlePrint();
            ConsoleView.showMessage("ticket remove: error");
        } else {
            boolean productRemoved = this.ticket.removeProduct(id);
            this.handlePrint();
            if (productRemoved) {
                ConsoleView.showMessage("ticket remove: ok");
            } else {
                ConsoleView.showMessage("ticket remove: error");
            }
        }
    }

    public void handlePrint() {
        ConsoleView.showTicket(this.ticket.toString());
        ConsoleView.showMessage("ticket print: ok");
    }

}
