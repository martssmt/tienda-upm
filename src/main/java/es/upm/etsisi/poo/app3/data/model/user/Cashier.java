package es.upm.etsisi.poo.app3.data.model.user;

import es.upm.etsisi.poo.app3.data.model.exceptions.EntityNotFoundException;
import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.products.CustomProduct;
import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cashier extends User {
    private final Map<String, Ticket> ticketList;
    private static final String FORMAT = "UW[0-9]{7}";

    public Cashier(String name, String mail) {
        super(name, mail);
        this.ticketList = new HashMap<>();
    }

    @Override
    public void setId(String id) {
        if (!id.matches(FORMAT)) {
            throw new InvalidAttributeException("Invalid cashierId");
        }
        this.id = id;
    }

    public void newTicket(Ticket ticket) {
        this.ticketList.put(ticket.getId(), ticket);
    }

    public void addProduct(String ticketId, Product product, Integer quantity) {
        Ticket ticket = this.ticketList.get(ticketId);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket not found");
        }
        ticket.add(product, quantity);
    }

    public void addCustomProduct(String ticketId, CustomProduct product, Integer quantity, String[] texts) {
        Ticket ticket = this.ticketList.get(ticketId);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket not found");
        }
        ticket.addCustom(product, quantity, texts);
    }

    public void removeProduct(String ticketId, Integer productId) {
        Ticket ticket = this.ticketList.get(ticketId);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket not found");
        }
        ticket.remove(productId);
    }

    public void closeTicket(String ticketId) {
        Ticket ticket = this.ticketList.get(ticketId);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket not found");
        }
        ticket.closeTicket();
    }

    public Ticket getTicket(String ticketId) {
        return this.ticketList.get(ticketId);
    }

    public List<Ticket> getTicketList() {
        return new ArrayList<>(this.ticketList.values());
    }

    @Override
    public String toString() {
        return "Cash{identifier='" + this.id + "', name='" + this.getName() + "', email='" + this.getMail() + "'}";
    }

}
