package es.upm.etsisi.poo.controller;

import es.upm.etsisi.poo.model.Category;
import es.upm.etsisi.poo.view.ConsoleView;

import java.util.Arrays;

public class CommandController {

    private final ProductController productController;
    private final TicketController ticketController;

    public CommandController(ProductController productController, TicketController ticketController) {
        this.productController = productController;
        this.ticketController = ticketController;
    }

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

    // Product:

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

    private void productList(String[] tokens) throws IllegalArgumentException {

        if (tokens.length != 2) {
            throw new IllegalArgumentException("Usage: prod list");
        }

        this.productController.handleList();

    }

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

    // Ticket:

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

    private void ticketNew(String[] tokens) throws IllegalArgumentException {

        if (tokens.length != 2) {
            throw new IllegalArgumentException("Usage: ticket new");
        }

        this.ticketController.handleNew();

    }

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

    // Echo:

    private void handleEcho(String input) {
        ConsoleView.showMessage(input);
    }

}