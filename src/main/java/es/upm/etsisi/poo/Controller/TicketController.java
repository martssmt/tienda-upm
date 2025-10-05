package es.upm.etsisi.poo.Controller;

import es.upm.etsisi.poo.Model.Catalog;
import es.upm.etsisi.poo.Model.Product;
import es.upm.etsisi.poo.Model.Ticket;
import es.upm.etsisi.poo.View.ConsoleView;

public class TicketController {

    private Ticket ticket;
    private ConsoleView view;
    private Catalog catalog;

    public TicketController(ConsoleView view, Catalog catalog) {
        this.view = view;
        this.catalog = catalog;
        this.ticket = new Ticket();
    }

    public void handleNew() {
        this.ticket.clear();
        this.view.showMessage("ticket new: ok");
    }

    public void handleAdd(int id, int quantity) {
        Product product = this.catalog.getProduct(id);
        if (product == null) {
            this.view.showMessage("Product with id " + id + " does not exist in the catalog.");
            this.handlePrint();
            this.view.showMessage("ticket add: error");
        } else {
            boolean productAdded = this.ticket.addProduct(product, quantity);
            this.handlePrint();
            if (productAdded) {
                this.view.showMessage("ticket add: ok");
            } else {
                this.view.showMessage("ticket add: error");
            }
        }
    }

    public void handleRemove(int id) {
        Product product = this.ticket.getProduct(id);
        if (product == null) {
            this.view.showMessage("Product with id " + id + " does not exist in the ticket.");
            this.handlePrint();
            this.view.showMessage("ticket remove: error");
        } else {
            boolean productRemoved = this.ticket.removeProduct(id);
            this.handlePrint();
            if (productRemoved) {
                this.view.showMessage("ticket remove: ok");
            } else {
                this.view.showMessage("ticket remove: error");
            }
        }
    }

    public void handlePrint() {
        this.view.showTicket(this.ticket.toString());
        this.view.showMessage("ticket print: ok");
    }

}
