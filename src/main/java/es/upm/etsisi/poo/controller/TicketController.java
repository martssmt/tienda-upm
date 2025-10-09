package es.upm.etsisi.poo.controller;

import es.upm.etsisi.poo.model.Catalog;
import es.upm.etsisi.poo.model.Product;
import es.upm.etsisi.poo.model.Ticket;
import es.upm.etsisi.poo.view.ConsoleView;

/**
 * The {@code TicketController} class manages all operations related to
 * the {@link Ticket} model.
 * <p>
 * It acts as the intermediary between user commands (handled by
 * {@link CommandController}) and the ticket data model, coordinating
 * actions such as creating a new ticket, adding or removing products,
 * and printing ticket information to the console.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *     <li>Create and clear tickets.</li>
 *     <li>Add and remove products from the ticket.</li>
 *     <li>Display ticket details using {@link ConsoleView}.</li>
 * </ul>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * Catalog catalog = new Catalog();
 * TicketController ticketController = new TicketController(catalog);
 *
 * ticketController.handleNew();
 * ticketController.handleAdd(1, 2);
 * ticketController.handlePrint();
 * }</pre>
 *
 * @author Marta
 * @version 1.0
 * @see Ticket
 * @see Catalog
 * @see Product
 * @see ConsoleView
 */

public class TicketController {

    /** The ticket instance currently managed by this controller. */
    private final Ticket ticket;

    /** Reference to the catalog used to retrieve product information. */
    private final Catalog catalog;

    /**
     * Constructs a {@code TicketController} that manages ticket operations
     * using the given catalog.
     *
     * @param catalog the {@link Catalog} instance used to access product data
     */
    public TicketController(Catalog catalog) {
        this.catalog = catalog;
        this.ticket = new Ticket();
    }

    /**
     * Creates a new empty ticket by clearing the current one.
     * Displays a confirmation message on the console.
     */
    public void handleNew() {
        this.ticket.clear();
        ConsoleView.showMessage("ticket new: ok");
    }

    /**
     * Adds a product from the catalog to the current ticket.
     * <p>
     * If the product ID does not exist in the catalog, an exception is thrown.
     * </p>
     *
     * @param id        the ID of the product to add
     * @param quantity  the quantity of the product to add
     * @throws IllegalStateException if the product ID is not found in the catalog
     *                               or if adding the product violates business rules
     * @see Ticket#addProduct(Product, int)
     */
    public void handleAdd(int id, int quantity) throws IllegalStateException {
        Product product = this.catalog.getProduct(id);
        if (product == null) {
            throw new IllegalStateException("No product found with ID " + id + " in the catalog");
        }
        this.ticket.addProduct(product, quantity);
        ConsoleView.showTicket(this.ticket);
        ConsoleView.showMessage("ticket add: ok");
    }

    /**
     * Removes a product from the current ticket.
     * <p>
     * If the product does not exist in the catalog or the ticket, an exception is thrown.
     * </p>
     *
     * @param id the ID of the product to remove
     * @throws IllegalStateException if the product is not found in the catalog
     * @see Ticket#removeProduct(Product)
     */
    public void handleRemove(int id) throws IllegalStateException {
        Product product = catalog.getProduct(id);
        if (product == null) {
            throw new IllegalStateException("No product found with ID " + id + " in the catalog");
        }
        this.ticket.removeProduct(product);
        ConsoleView.showTicket(this.ticket);
        ConsoleView.showMessage("ticket remove: ok");
    }

    /**
     * Displays the contents of the current ticket on the console.
     * <p>
     * This includes all products, quantities, total price, and applied discounts.
     * </p>
     */
    public void handlePrint() {
        ConsoleView.showTicket(this.ticket);
        ConsoleView.showMessage("ticket print: ok");
    }
}