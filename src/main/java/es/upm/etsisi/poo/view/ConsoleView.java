package es.upm.etsisi.poo.view;

import es.upm.etsisi.poo.model.Catalog;
import es.upm.etsisi.poo.model.Product;
import es.upm.etsisi.poo.model.Ticket;

/**
 * The {@code ConsoleView} class represents the view layer of the application.
 * <p>
 * It is responsible for displaying messages, lists, and objects to the console.
 * This class acts as a simple text-based user interface for the system,
 * used by the controllers to interact with the user.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * ConsoleView.showMessage("Welcome to the app!");
 * ConsoleView.showProduct(product);
 * ConsoleView.showCatalog(catalog);
 * ConsoleView.showTicket(ticket);
 * }</pre>
 *
 * @author Sofia
 * @author Alicia
 * @version 1.0
 * @see Product
 * @see Catalog
 * @see Ticket
 */

public class ConsoleView {

    /**
     * Displays a general message to the console.
     *
     * @param message the message to be displayed
     */
    public static void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays the list of available commands and usage instructions.
     * <p>
     * This method is typically invoked when the user types the {@code help} command.
     * </p>
     */
    public static void showHelp() {
        final String MESSAGE = """
                Commands:
                 prod add <id> "<name>" <category> <price>
                 prod list
                 prod update <id> NAME|CATEGORY|PRICE <value>
                 prod remove <id>
                 ticket new
                 ticket add <prodId> <quantity>
                 ticket remove <prodId>
                 ticket print
                 echo "<text>"
                 help
                 exit
                 
                Categories: MERCH, STATIONERY, CLOTHES, BOOKS, ELECTRONICS
                Discounts if there are ≥2 units in the category:
                MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOKS 10%, ELECTRONICS 3%.""";
        System.out.println(MESSAGE);
    }

    /**
     * Displays a single product's details to the console.
     *
     * @param product the {@link Product} to be displayed;
     *                if {@code null}, nothing is printed
     */
    public static void showProduct(Product product) {
        if (product != null) {
            System.out.println(product);
        }
    }

    /**
     * Displays the details of a ticket to the console.
     *
     * @param ticket the {@link Ticket} to be displayed
     */
    public static void showTicket(Ticket ticket) {
        System.out.println(ticket);
    }

    /**
     * Displays the full catalog, including all registered products.
     *
     * @param catalog the {@link Catalog} to be displayed
     */
    public static void showCatalog(Catalog catalog) {
        System.out.println(catalog);
    }
}