package es.upm.etsisi.poo.Controller;

import es.upm.etsisi.poo.Model.Category;
import es.upm.etsisi.poo.View.ConsoleView;

import java.util.Arrays;

public class CommandController {

    private final ProductController productController;
    private final TicketController ticketController;

    public CommandController(ProductController productController, TicketController ticketController) {
        this.productController = productController;
        this.ticketController = ticketController;
    }

    public void parseCommand(String input) {

        if (input == null || input.trim().isEmpty()) return;

        String[] tokens = input.trim().split(" ");

        switch (tokens[0].toLowerCase()) {
            case "prod" -> this.handleProduct(tokens, input);
            case "ticket" -> this.handleTicket(tokens);
            case "help" -> ConsoleView.showHelp();
            case "echo" -> this.handleEcho(tokens);
            case "exit" -> {
                ConsoleView.showMessage("Closing application.");
                ConsoleView.showMessage("Goodbye!");
                System.exit(0);
            }
            default -> ConsoleView.showMessage("Unknown command: " + input);
        }

    }

    // Product:

    private void handleProduct(String[] tokens, String input) {

        if (tokens.length < 2) {
            ConsoleView.showMessage("Error: prod command needs arguments");
            return;
        }

        switch (tokens[1].toLowerCase()) {
            case "add" -> this.productAdd(tokens, input);
            case "list" -> this.productList(tokens);
            case "update" -> this.productUpdate(tokens);
            case "remove" -> this.productRemove(tokens);
            default -> ConsoleView.showMessage("Unknown command: " + input);
        }

    }

    private void productAdd(String[] tokens, String input) {
        try {
            int firstQuote = input.indexOf('"');
            int lastQuote = input.lastIndexOf('"');
            if (firstQuote == -1 || firstQuote == lastQuote) {
                throw new IllegalArgumentException("Name must be in quotes");
            }

            String name = input.substring(firstQuote + 1, lastQuote);
            String[] beforeName = input.substring(0, firstQuote).split(" ");
            String[] afterName = input.substring(lastQuote + 1).trim().split(" ");
            if (afterName.length != 2 || beforeName.length != 3) {
                throw new IllegalArgumentException("Usage: prod add <id> \\\"<name>\\\" <category> <price>");
            }

            int id = Integer.parseInt(beforeName[2]);
            Category category = Category.valueOf(afterName[0].toUpperCase());
            double price = Double.parseDouble(afterName[1]);

            this.productController.handleAdd(id, name, category, price);
        } catch (NumberFormatException e) {
            ConsoleView.showMessage("Error: Type mismatch for argument.");
        } catch (Exception e) {
            ConsoleView.showMessage("Error in prod add: " + e.getMessage());
        }
    }

    private void productList(String[] tokens) {
        try {
            if (tokens.length != 2) {
                throw new IllegalArgumentException("Usage: prod list");
            }
            this.productController.handleList();
        } catch (Exception e) {
            ConsoleView.showMessage("Error in prod list: " + e.getMessage());
        }
    }

    private void productUpdate(String[] tokens) {
        try {
            if (tokens.length < 5) {
                throw new IllegalArgumentException("Usage: prod update <id> NAME|CATEGORY|PRICE <value>");
            }
            int idUpdate = Integer.parseInt(tokens[2]);
            String field = tokens[3];
            if (!field.equals("NAME") && !field.equals("CATEGORY") && !field.equals("PRICE")) {
                throw new IllegalArgumentException("Field must be NAME, CATEGORY or PRICE");
            }
            String value = String.join(" ", Arrays.copyOfRange(tokens, 4, tokens.length));
            this.productController.handleUpdate(idUpdate, field, value);
        } catch (NumberFormatException e) {
            ConsoleView.showMessage("Error: Type mismatch for argument.");
        } catch (Exception e) {
            ConsoleView.showMessage("Error in prod update: " + e.getMessage());
        }
    }

    private void productRemove(String[] tokens) {
        try {
            if (tokens.length != 3) {
                throw new IllegalArgumentException("Usage: prod remove <id>");
            }
            int idRemove = Integer.parseInt(tokens[2]);
            this.productController.handleRemove(idRemove);
        } catch (NumberFormatException e) {
            ConsoleView.showMessage("Error: Type mismatch for argument.");
        } catch (Exception e) {
            ConsoleView.showMessage("Error in prod remove: " + e.getMessage());
        }
    }

    // Ticket:

    private void handleTicket(String[] tokens) {

        if (tokens.length < 2) {
            ConsoleView.showMessage("Error: ticket command needs arguments");
            return;
        }

        switch (tokens[1].toLowerCase()) {
            case "new" -> this.ticketNew(tokens);
            case "add" -> this.ticketAdd(tokens);
            case "remove" -> this.ticketRemove(tokens);
            case "print" -> this.ticketController.handlePrint();
            default ->
                    ConsoleView.showMessage("Unknown command: " + String.join(" ", Arrays.copyOfRange(tokens, 0, tokens.length)));
        }

    }

    private void ticketNew(String[] tokens) {
        try {
            if (tokens.length != 2) {
                throw new IllegalArgumentException("Usage: ticket new");
            }
            this.ticketController.handleNew();
        } catch (Exception e) {
            ConsoleView.showMessage("Error in ticket new: " + e.getMessage());
        }
    }

    private void ticketAdd(String[] tokens) {
        try {
            if (tokens.length != 4) {
                throw new IllegalArgumentException("Usage: ticket add <prodId> <quantity>");
            }
            int idAdd = Integer.parseInt(tokens[2]);
            int quantity = Integer.parseInt(tokens[3]);
            this.ticketController.handleAdd(idAdd, quantity);
        } catch (NumberFormatException e) {
            ConsoleView.showMessage("Error: Type mismatch for argument.");
        } catch (Exception e) {
            ConsoleView.showMessage("Error in ticket add: " + e.getMessage());
        }
    }

    private void ticketRemove(String[] tokens) {
        try {
            if (tokens.length < 3) {
                throw new IllegalArgumentException("Usage: ticket remove <prodId>");
            }
            int idRemove = Integer.parseInt(tokens[2]);
            this.ticketController.handleRemove(idRemove);
        } catch (NumberFormatException e) {
            ConsoleView.showMessage("Error: Type mismatch for argument.");
        } catch (Exception e) {
            ConsoleView.showMessage("Error in ticket remove: " + e.getMessage());
        }
    }

    // Echo

    private void handleEcho(String[] tokens) {
        ConsoleView.showMessage(String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length)));
    }

}