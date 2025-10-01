package es.upm.etsisi.poo.Controller;

import es.upm.etsisi.poo.Model.Catalog;
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
        ticket.clear();
        this.view.showMessage("ticket new: ok");
    }

}
