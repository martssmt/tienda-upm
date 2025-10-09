package es.upm.etsisi.poo.controller;

import es.upm.etsisi.poo.model.Category;
import es.upm.etsisi.poo.view.ConsoleView;

import java.util.Arrays;

/**
 * The {@code CommandController} class acts as the command interpreter for the
 * console-based user interface.
 * <p>
 * It parses and validates user input, delegates commands to the appropriate
 * controllers ({@link ProductController} and {@link TicketController}),
 * and handles input errors gracefully by displaying descriptive messages
 * through the {@link ConsoleView}.
 * </p>
 *
 * <p>
 * This class serves as the central hub that coordinates all user actions
 * in the application.
 * </p>
 *
 * <p><b>Supported command categories:</b></p>
 * <ul>
 *     <li><b>prod</b> – product management (add, list, update, remove)</li>
 *     <li><b>ticket</b> – ticket operations (new, add, remove, print)</li>
 *     <li><b>echo</b> – prints a message back to the console</li>
 *     <li><b>help</b> – displays all available commands</li>
 *     <li><b>exit</b> – terminates the application</li>
 * </ul>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * CommandController commandController = new CommandController(productCtrl, ticketCtrl);
 * commandController.parseCommand("prod add 1 \"Book\" BOOK 29.99");
 * commandController.parseCommand("ticket new");
 * commandController.parseCommand("exit");
 * }</pre>
 *
 * @author Marta
 * @version 1.0
 * @see ProductController
 * @see TicketController
 * @see ConsoleView
 */

public class CommandController {

    /** The controller responsible for product-related operations. */
    private final ProductController productController;

    /** The controller responsible for ticket-related operations. */
    private final TicketController ticketController;

    /**
     * Constructs a {@code CommandController} with the given controllers.
     *
     * @param productController the {@link ProductController} used to manage products
     * @param ticketController  the {@link TicketController} used to manage tickets
     */
    public CommandController(ProductController productController, TicketController ticketController) {
        this.productController = productController;
        this.ticketController = ticketController;
    }

    /**
     * Parses and executes a command entered by the user.
     * <p>
     * Commands are categorized by their first keyword (e.g., {@code prod}, {@code ticket}),
     * and delegated to the corresponding handler methods.
     * </p>
     *
     * @param input the full command line entered by the user
     * @return {@code false} if the command is {@code exit}, otherwise {@code true}
     */
    public boolean parseCommand(String input) {
        if (input == null || input.trim().isEmpty()) return true;

        String[] tokens = input.trim().split(" ");

        try {
            switch (tokens[0].toLowerCase()) {
                case "prod" -> this.handleProduct(tokens, input);
                case "ticket" -> this.handleTicket(tokens, input);
                case "help" -> ConsoleView.showHelp();
                case "echo" -> this.handleEcho(input);
                case "exit" -> {
                    return false;
                }
                default -> ConsoleView.showMessage("Unknown command: " + input);
            }
        } catch (IllegalArgumentException e) {
            ConsoleView.showMessage("Illegal Argument Exception: " + e.getMessage());
            ConsoleView.showMessage(tokens[0] + " " + tokens[1] + ": error");
        } catch (Exception e) {
            ConsoleView.showMessage("Error: " + e.getMessage());
            ConsoleView.showMessage(tokens[0] + " " + tokens[1] + ": error");
        }
        return true;
    }

    // ──────────────────────────────────────────────
    // Product command handlers
    // ──────────────────────────────────────────────

    /**
     * Handles all product-related commands.
     *
     * @param tokens the parsed command tokens
     * @param input  the full raw input string
     * @throws IllegalArgumentException if the command syntax is invalid
     */
    private void handleProduct(String[] tokens, String input) throws IllegalArgumentException {
        if (tokens.length < 2) {
            throw new IllegalArgumentException("prod command needs arguments");
        }

        switch (tokens[1].toLowerCase()) {
            case "add" -> this.productAdd(input);
            case "list" -> this.productList(tokens);
            case "update" -> this.productUpdate(tokens);
            case "remove" -> this.productRemove(tokens);
            default -> ConsoleView.showMessage("Unknown command: " + input);
        }
    }

    /**
     * Adds a product to the catalog based on parsed parameters.
     *
     * @param input the raw input command string
     * @throws IllegalArgumentException if the command syntax or arguments are invalid
     */
    private void productAdd(String input) throws IllegalArgumentException {
        if (input.split(" ").length == 2) {
            throw new IllegalArgumentException("Usage: prod add <id> \"<name>\" <category> <price>");
        }

        int firstQuote = input.indexOf('"');
        int lastQuote = input.lastIndexOf('"');

        if (firstQuote == -1 || firstQuote == lastQuote) {
            throw new IllegalArgumentException("Name must be in quotes");
        }

        String name = input.substring(firstQuote + 1, lastQuote);
        String[] beforeName = input.substring(0, firstQuote - 1).split(" ");
        String[] afterName = input.substring(lastQuote + 1).trim().split(" ");

        if (afterName.length != 2 || beforeName.length != 3) {
            throw new IllegalArgumentException("Usage: prod add <id> \"<name>\" <category> <price>");
        }

        int id;
        Category category;
        double price;
        try {
            id = Integer.parseInt(beforeName[2]);
            category = Category.valueOf(afterName[0].toUpperCase());
            price = Double.parseDouble(afterName[1]);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Product ID and price must be integers");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + afterName[0]);
        }

        if (id <= 0) {
            throw new IllegalArgumentException("The product ID must be positive");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("The product price must be positive");
        }

        this.productController.handleAdd(id, name, category, price);
    }

    /**
     * Displays the list of all products in the catalog.
     *
     * @param tokens the parsed command tokens
     * @throws IllegalArgumentException if the command syntax is invalid
     */
    private void productList(String[] tokens) throws IllegalArgumentException {
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Usage: prod list");
        }
        this.productController.handleList();
    }

    /**
     * Updates an existing product’s field (name, category, or price).
     *
     * @param tokens the parsed command tokens
     * @throws IllegalArgumentException if arguments are missing or invalid
     */
    private void productUpdate(String[] tokens) throws IllegalArgumentException {
        if (tokens.length < 5) {
            throw new IllegalArgumentException("Usage: prod update <id> NAME|CATEGORY|PRICE <value>");
        }

        int id;
        try {
            id = Integer.parseInt(tokens[2]);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Product ID must be an integer");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("The product ID must be positive");
        }

        String field = tokens[3];
        if (!field.equals("NAME") && !field.equals("CATEGORY") && !field.equals("PRICE")) {
            throw new IllegalArgumentException("Field must be NAME, CATEGORY or PRICE");
        }

        String value = String.join(" ", Arrays.copyOfRange(tokens, 4, tokens.length));
        this.productController.handleUpdate(id, field, value);
    }

    /**
     * Removes a product from the catalog by its ID.
     *
     * @param tokens the parsed command tokens
     * @throws IllegalArgumentException if the command syntax or ID is invalid
     */
    private void productRemove(String[] tokens) throws IllegalArgumentException {
        if (tokens.length != 3) {
            throw new IllegalArgumentException("Usage: prod remove <id>");
        }

        try {
            int id = Integer.parseInt(tokens[2]);
            if (id <= 0) {
                throw new IllegalArgumentException("The product ID must be positive");
            }
            this.productController.handleRemove(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Product ID must be an integer");
        }
    }

    // ──────────────────────────────────────────────
    // Ticket command handlers
    // ──────────────────────────────────────────────

    /**
     * Handles all ticket-related commands.
     *
     * @param tokens the parsed command tokens
     * @param input  the full raw input string
     * @throws IllegalArgumentException if the command syntax is invalid
     */
    private void handleTicket(String[] tokens, String input) throws IllegalArgumentException {
        if (tokens.length < 2) {
            throw new IllegalArgumentException("ticket command needs arguments");
        }

        switch (tokens[1].toLowerCase()) {
            case "new" -> this.ticketNew(tokens);
            case "add" -> this.ticketAdd(tokens);
            case "remove" -> this.ticketRemove(tokens);
            case "print" -> this.ticketController.handlePrint();
            default -> ConsoleView.showMessage("Unknown command: " + input);
        }
    }

    /**
     * Creates a new ticket.
     *
     * @param tokens the parsed command tokens
     * @throws IllegalArgumentException if the command syntax is invalid
     */
    private void ticketNew(String[] tokens) throws IllegalArgumentException {
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Usage: ticket new");
        }
        this.ticketController.handleNew();
    }

    /**
     * Adds a product to the current ticket.
     *
     * @param tokens the parsed command tokens
     * @throws IllegalArgumentException if the command syntax, ID, or quantity is invalid
     */
    private void ticketAdd(String[] tokens) throws IllegalArgumentException {
        if (tokens.length != 4) {
            throw new IllegalArgumentException("Usage: ticket add <prodId> <quantity>");
        }

        try {
            int id = Integer.parseInt(tokens[2]);
            if (id <= 0) {
                throw new IllegalArgumentException("The product ID must be positive");
            }
            int quantity = Integer.parseInt(tokens[3]);
            this.ticketController.handleAdd(id, quantity);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Product ID and quantity must be integers");
        }
    }

    /**
     * Removes a product from the current ticket by its ID.
     *
     * @param tokens the parsed command tokens
     * @throws IllegalArgumentException if the command syntax or ID is invalid
     */
    private void ticketRemove(String[] tokens) throws IllegalArgumentException {
        if (tokens.length < 3) {
            throw new IllegalArgumentException("Usage: ticket remove <prodId>");
        }

        try {
            int id = Integer.parseInt(tokens[2]);
            if (id <= 0) {
                throw new IllegalArgumentException("The product ID must be positive");
            }
            this.ticketController.handleRemove(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Product ID must be an integer");
        }
    }

    // ──────────────────────────────────────────────
    // Echo command
    // ──────────────────────────────────────────────

    /**
     * Handles the {@code echo} command, printing text directly to the console.
     *
     * @param input the full command line entered by the user
     */
    private void handleEcho(String input) {
        ConsoleView.showMessage(input);
    }
}