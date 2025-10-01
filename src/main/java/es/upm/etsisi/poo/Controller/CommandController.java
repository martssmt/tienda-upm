package es.upm.etsisi.poo.Controller;

import es.upm.etsisi.poo.Model.Category;
import es.upm.etsisi.poo.View.ConsoleView;

import java.util.Arrays;

public class CommandController {

    private final ProductController productController;
    private final TicketController ticketController;
    private final ConsoleView view;

    public CommandController(ProductController productController, TicketController ticketController, ConsoleView view) {
        this.productController = productController;
        this.ticketController = ticketController;
        this.view = view;
    }

    public void parseCommand(String input) {

        if (input == null || input.trim().isEmpty()) return;

        String[] tokens = input.trim().split(" ");

        switch (tokens[0].toLowerCase()) {
            case "prod":
                this.handleProduct(tokens, input);
                break;
            case "ticket":
                this.handleTicket(tokens, input);
                break;
            case "help":
                this.view.showHelp();
                break;
            case "echo":
                this.handleEcho(tokens);
                break;
            case "exit":
                this.view.showMessage("Closing application.");
                this.view.showMessage("Goodbye!");
                System.exit(0);
                break;
            default:
                this.view.showMessage("Unknown command: " + input);
                break;
        }

    }

    private void handleProduct(String[] tokens, String input) {

        if (tokens.length < 2) {
            this.view.showMessage("Error: prod command needs arguments");
            return;
        }

        switch (tokens[1].toLowerCase()) {
            case "add":
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
                } catch (Exception e) {
                    this.view.showMessage("Error in prod add: " + e.getMessage());
                }
                break;
            case "list":
                try {
                    if (tokens.length != 2) {
                        throw new IllegalArgumentException("Usage: prod list");
                    }
                    this.productController.handleList();
                } catch (Exception e) {
                    this.view.showMessage("Error in prod list: " + e.getMessage());
                }
                break;
            case "update":
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
                } catch (Exception e) {
                    view.showMessage("Error in prod update: " + e.getMessage());
                }
                break;
            case "remove":
                try {
                    if (tokens.length != 3) {
                        throw new IllegalArgumentException("Usage: prod remove <id>");
                    }
                    int idRemove = Integer.parseInt(tokens[2]);
                    this.productController.handleRemove(idRemove);
                } catch (Exception e) {
                    this.view.showMessage("Error in prod remove: " + e.getMessage());
                }
                break;
            case default:
                this.view.showMessage("Unknown command: " + input);
                break;
        }

    }

    private void handleTicket(String[] tokens, String input) {

        if (tokens.length < 2) {
            this.view.showMessage("Error: ticket command needs arguments");
            return;
        }

        switch (tokens[1].toLowerCase()) {
            case "new":
                try {
                    if (tokens.length != 2) {
                        throw new IllegalArgumentException("Usage: ticket new");
                    }
                    this.ticketController.handleNew();
                } catch (Exception e) {
                    this.view.showMessage("Error in ticket new: " + e.getMessage());
                }
                break;
            case "add":
                try {
                    if (tokens.length != 4) {
                        throw new IllegalArgumentException("Usage: ticket add <prodId> <quantity>");
                    }
                    int idAdd = Integer.parseInt(tokens[2]);
                    int quantity = Integer.parseInt(tokens[3]);
                    this.ticketController.handleAdd(idAdd, quantity);
                } catch (Exception e) {
                    view.showMessage("Error in ticket add: " + e.getMessage());
                }
                break;
            case "remove":
                try {
                    if (tokens.length < 3) {
                        throw new IllegalArgumentException("Usage: ticket remove <prodId>");
                    }
                    int idRemove = Integer.parseInt(tokens[2]);
                    this.ticketController.handleRemove(idRemove);
                } catch (Exception e) {
                    view.showMessage("Error in ticket remove: " + e.getMessage());
                }
                break;
            case "print":
                this.ticketController.handlePrint();
                break;
            case default:
                this.view.showMessage("Unknown command: " + String.join(" ", Arrays.copyOfRange(tokens, 0, tokens.length)));
                break;
        }

    }

    private void handleEcho(String[] tokens) {
        this.view.showMessage(String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length)));
    }

}