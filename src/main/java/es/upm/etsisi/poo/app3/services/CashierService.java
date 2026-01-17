package es.upm.etsisi.poo.app3.services;

import es.upm.etsisi.poo.app3.data.model.shop.products.CustomProduct;
import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.data.model.user.Cashier;
import es.upm.etsisi.poo.app3.data.model.user.Client;
import es.upm.etsisi.poo.app3.data.repositories.CashierRepository;
import es.upm.etsisi.poo.app3.data.repositories.ClientRepository;
import es.upm.etsisi.poo.app3.services.exceptions.DuplicateException;
import es.upm.etsisi.poo.app3.services.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for cashier-related operations.
 * <p>
 * This service coordinates application-level use cases involving {@link Cashier},
 * {@link Client}, and {@link Ticket} entities. It acts as a façade over the
 * {@link CashierRepository} and {@link ClientRepository}, enforcing business rules
 * and orchestrating ticket management workflows.
 * </p>
 *
 * <p>
 * Responsibilities include cashier registration, ticket creation, ticket printing,
 * and product management within tickets.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Cashier
 * @see Ticket
 * @see CashierRepository
 * @see ClientRepository
 */
public class CashierService implements Service<Cashier> {

    /**
     * Repository used to persist and retrieve cashier entities.
     */
    private final CashierRepository cashierRepository;

    /**
     * Repository used to persist and retrieve client entities.
     */
    private final ClientRepository clientRepository;

    /**
     * Creates a new cashier service using the given repositories.
     *
     * @param cashierRepository the cashier repository
     * @param clientRepository  the client repository
     */
    public CashierService(CashierRepository cashierRepository, ClientRepository clientRepository) {
        this.cashierRepository = cashierRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * Registers a new cashier with the given identifier.
     *
     * @param cashier the cashier to register
     * @param id      the identifier to assign
     * @throws DuplicateException if a cashier with the same identifier already exists
     */
    @Override
    public void add(Cashier cashier, String id) {
        if (this.cashierRepository.findById(id) != null) {
            throw new DuplicateException("There is already a cashier with id " + id + " registered.");
        }
        this.cashierRepository.add(cashier, id);
    }

    /**
     * Removes a cashier from the system by its identifier.
     *
     * @param id the identifier of the cashier to remove
     * @return the removed cashier
     * @throws NotFoundException if no cashier with the given identifier exists
     */
    @Override
    public Cashier remove(String id) {
        Cashier cashier = this.cashierRepository.findById(id);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + id + " registered.");
        }
        this.cashierRepository.remove(id);
        return cashier;
    }

    /**
     * Retrieves all registered cashiers.
     *
     * @return the list of cashiers
     */
    @Override
    public List<Cashier> list() {
        return this.cashierRepository.list();
    }

    /**
     * Registers a new cashier with automatically generated identifier.
     * <p>
     * Duplicate email validation is intentionally omitted to comply with
     * input test requirements.
     * </p>
     *
     * @param cashier the cashier to register
     */
    public void add(Cashier cashier) {
        this.cashierRepository.add(cashier);
    }

    /**
     * Creates a new ticket for a given cashier and client.
     * <p>
     * The ticket is associated with both entities and persisted through
     * repository updates.
     * </p>
     *
     * @param ticket    the ticket to create
     * @param cashierId the cashier identifier
     * @param clientId  the client identifier
     * @throws NotFoundException if the cashier or client does not exist
     */
    public void newTicket(Ticket ticket, String cashierId, String clientId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        Client client = this.clientRepository.findById(clientId);

        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        if (client == null) {
            throw new NotFoundException("There is no client with id " + clientId + " registered.");
        }

        client.addTicket(ticket.getId());
        cashier.newTicket(ticket);

        this.cashierRepository.update(cashier);
        this.clientRepository.update(client);
    }

    /**
     * Closes and prints a ticket managed by a cashier.
     *
     * @param cashierId the cashier identifier
     * @param ticketId  the ticket identifier
     * @return the closed ticket
     * @throws NotFoundException if the cashier does not exist
     */
    public Ticket print(String cashierId, String ticketId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }

        cashier.closeTicket(ticketId);
        this.cashierRepository.update(cashier);
        return cashier.getTicket(ticketId);
    }

    /**
     * Adds a purchasable element to a ticket.
     *
     * @param cashierId   the cashier identifier
     * @param ticketId    the ticket identifier
     * @param purchasable the purchasable element to add
     * @param quantity    the quantity to add
     * @return the updated ticket
     * @throws NotFoundException if the cashier does not exist
     */
    public Ticket addProduct(String cashierId, String ticketId,
                             Purchasable purchasable, Integer quantity) {

        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }

        cashier.addProduct(ticketId, purchasable, quantity);
        this.cashierRepository.update(cashier);
        return cashier.getTicket(ticketId);
    }

    /**
     * Adds a custom product to a ticket.
     *
     * @param cashierId the cashier identifier
     * @param ticketId  the ticket identifier
     * @param product   the custom product to add
     * @param quantity  the quantity to add
     * @param texts     the customization texts
     * @return the updated ticket
     * @throws NotFoundException if the cashier does not exist
     */
    public Ticket addCustomProduct(String cashierId, String ticketId,
                                   CustomProduct product, Integer quantity, String[] texts) {

        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }

        cashier.addCustomProduct(ticketId, product, quantity, List.of(texts));
        this.cashierRepository.update(cashier);
        return cashier.getTicket(ticketId);
    }

    /**
     * Removes a product from a ticket.
     *
     * @param cashierId the cashier identifier
     * @param ticketId  the ticket identifier
     * @param prodId    the product identifier
     * @return the updated ticket
     * @throws NotFoundException if the cashier does not exist
     */
    public Ticket removeProduct(String cashierId, String ticketId, String prodId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }

        cashier.removeProduct(ticketId, prodId);
        this.cashierRepository.update(cashier);
        return cashier.getTicket(ticketId);
    }

    /**
     * Retrieves a list of all tickets in the system with their current status.
     *
     * @return a list of ticket summaries
     */
    public List<String> ticketList() {
        ArrayList<String> tickets = new ArrayList<>();
        for (Ticket t : this.cashierRepository.listTickets()) {
            tickets.add(t.getName() + " - " + t.getStatus());
        }
        return tickets;
    }

    /**
     * Retrieves a list of tickets managed by a specific cashier.
     *
     * @param cashierId the cashier identifier
     * @return a list of ticket summaries for the cashier
     * @throws NotFoundException if the cashier does not exist
     */
    public List<String> ticketListFromCashier(String cashierId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }

        ArrayList<String> tickets = new ArrayList<>();
        for (Ticket t : cashier.getTicketList()) {
            tickets.add(t.getName() + " -> " + t.getStatus());
        }
        return tickets;
    }
}