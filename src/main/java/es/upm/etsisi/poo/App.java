package es.upm.etsisi.poo;

import es.upm.etsisi.poo.controller.*;
import es.upm.etsisi.poo.model.Catalog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The {@code App} class serves as the main entry point of the Ticket Management application.
 * <p>
 * It initializes the core components of the MVC architecture, including controllers
 * for managing products, tickets, and user commands.
 * <p>
 * The application provides a text-based user interface through the console,
 * allowing users to execute commands to manage a product catalog and generate tickets.
 * </p>
 *
 * <p><b>Execution flow:</b></p>
 * <ol>
 *   <li>Initializes the controllers ({@link ProductController}, {@link TicketController},
 *   and {@link CommandController}).</li>
 *   <li>Displays a welcome message and command usage instructions.</li>
 *   <li>Enters a continuous input loop where user commands are read and processed.</li>
 *   <li>Terminates gracefully when the user issues the {@code exit} command.</li>
 * </ol>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * java -jar tiendaUPM.jar
 *
 * Output:
 * Welcome to the ticket module App.
 * Ticket module. Type 'help' to see commands.
 * tUPM> prod add 1 "Java Book" BOOK 25.99
 * tUPM> ticket new
 * tUPM> exit
 * Closing application.
 * Goodbye!
 * }</pre>
 *
 * @author Sofia
 * @author Alicia
 * @version 1.0
 * @see ProductController
 * @see TicketController
 * @see CommandController
 * @see Catalog
 */

public class App {

    /**
     * The main method that starts the application.
     * <p>
     * It initializes all necessary controllers, sets up console input,
     * and continuously processes user commands until the user chooses to exit.
     * </p>
     *
     * @param args command-line arguments (not used)
     * @throws RuntimeException if an {@link IOException} occurs while reading user input
     */
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            ProductController productController = new ProductController();
            Catalog catalog = productController.getCatalog();
            TicketController ticketController = new TicketController(catalog);
            CommandController commandController = new CommandController(productController, ticketController);

            boolean running = true;

            System.out.println("Welcome to the ticket module App.");
            System.out.println("Ticket module. Type 'help' to see commands.");

            while (running) {
                System.out.print("\ntUPM> ");
                String line = reader.readLine();
                running = commandController.parseCommand(line);
            }

            System.out.println("Closing application.");
            System.out.println("Goodbye!");

            reader.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}