package es.upm.etsisi.poo.app3.data.model.user;

import es.upm.etsisi.poo.app3.data.model.exceptions.EntityNotFoundException;
import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.products.CustomProduct;
import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code Cashier} class represents a cashier user in the store system.
 * <p>
 * A cashier is a concrete specialization of {@link User} responsible for
 * managing tickets, including their creation, modification, and closure.
 * Each cashier owns a collection of tickets identified by their unique IDs.
 * </p>
 *
 * <p>
 * Tickets managed by a cashier are persisted using a one-to-many relationship,
 * ensuring that their lifecycle is tightly coupled to the cashier entity.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * Cashier cashier = new Cashier("Bob", "bob@upm.es");
 * cashier.setId("UW1234567");
 * cashier.newTicket(ticket);
 * cashier.addProduct(ticket.getId(), product, 2);
 * cashier.closeTicket(ticket.getId());
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 * @see User
 * @see Ticket
 */
@jakarta.persistence.Entity
@Table(name = "cashiers")
public class Cashier extends User {

    /**
     * Map of tickets managed by the cashier, indexed by ticket identifier.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cashier_id")
    @MapKey(name = "id")
    private Map<String, Ticket> ticketList;

    /**
     * Regular expression defining the valid cashier identifier format.
     */
    private static final String FORMAT = "UW[0-9]{7}";

    /**
     * Protected no-argument constructor required by JPA.
     */
    protected Cashier() {
        super();
    }

    /**
     * Creates a new cashier with the given name and email address.
     *
     * @param name the cashier's name
     * @param mail the cashier's email address
     */
    public Cashier(String name, String mail) {
        super(name, mail);
        this.ticketList = new HashMap<>();
    }

    /**
     * Assigns the identifier of the cashier.
     *
     * @param id the cashier identifier
     * @throws InvalidAttributeException if the identifier format is invalid
     */
    @Override
    public void setId(String id) {
        if (!id.matches(FORMAT)) {
            throw new InvalidAttributeException("Invalid cashierId");
        }
        this.id = id;
    }

    /**
     * Creates and registers a new ticket for the cashier.
     *
     * @param ticket the ticket to be added
     */
    public void newTicket(Ticket ticket) {
        this.ticketList.put(ticket.getId(), ticket);
    }

    /**
     * Adds a purchasable product to a ticket.
     *
     * @param ticketId    the ticket identifier
     * @param purchasable the product or service to add
     * @param quantity    the quantity to add
     * @throws EntityNotFoundException if the ticket does not exist
     */
    public void addProduct(String ticketId, Purchasable<?> purchasable, Integer quantity) {
        Ticket ticket = this.ticketList.get(ticketId);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket not found");
        }
        ticket.add(purchasable, quantity);
    }

    /**
     * Adds a custom product to a ticket.
     *
     * @param ticketId the ticket identifier
     * @param product  the custom product
     * @param quantity the quantity to add
     * @param texts    additional custom texts associated with the product
     * @throws EntityNotFoundException if the ticket does not exist
     */
    public void addCustomProduct(String ticketId, CustomProduct product, Integer quantity, List<String> texts) {
        Ticket ticket = this.ticketList.get(ticketId);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket not found");
        }
        ticket.addCustom(product, quantity, texts);
    }

    /**
     * Removes a product from a ticket.
     *
     * @param ticketId  the ticket identifier
     * @param productId the product identifier
     * @throws EntityNotFoundException if the ticket does not exist
     */
    public void removeProduct(String ticketId, String productId) {
        Ticket ticket = this.ticketList.get(ticketId);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket not found");
        }
        ticket.remove(productId);
    }

    /**
     * Closes a ticket managed by the cashier.
     *
     * @param ticketId the ticket identifier
     * @throws EntityNotFoundException if the ticket does not exist
     */
    public void closeTicket(String ticketId) {
        Ticket ticket = this.ticketList.get(ticketId);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket not found");
        }
        ticket.closeTicket();
    }

    /**
     * Returns a specific ticket managed by the cashier.
     *
     * @param ticketId the ticket identifier
     * @return the ticket, or {@code null} if it does not exist
     */
    public Ticket getTicket(String ticketId) {
        return this.ticketList.get(ticketId);
    }

    /**
     * Returns a list of all tickets managed by the cashier.
     *
     * @return the list of tickets
     */
    public List<Ticket> getTicketList() {
        return new ArrayList<>(this.ticketList.values());
    }

    /**
     * Returns a string representation of the cashier.
     *
     * @return a textual representation of the cashier
     */
    @Override
    public String toString() {
        return "Cash{identifier='" + this.id + "', name='" + this.getName() + "', email='" + this.getMail() + "'}";
    }
}