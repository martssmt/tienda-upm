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

        String[] tokens = input.trim().split(" ");
        if (tokens.length == 0) return;

        switch (tokens[0].toLowerCase()) {
            case "prod":
                handleProduct(tokens);
                break;
            case "ticket":
                handleTicket(tokens);
                break;
            case "help":
                view.showHelp();
                break;
            case "echo":
                handleEcho(tokens);
                break;
            case "exit":
                System.exit(0);
                break;
            default:
                view.showMessage("Unknown command: " + input);
                break;
        }

    }

    private void handleProduct(String[] tokens) {
        switch (tokens[1].toLowerCase()) {
            case "add":
                int id = Integer.parseInt(tokens[2]);
                String name = tokens[3].replace("\"", "");
                Category category = Category.valueOf(tokens[4].toUpperCase());
                double price = Double.parseDouble(tokens[5]);
                productController.handleAdd(id, name, category, price);
                break;
            case "list":
                productController.handleList();
                break;
            case "update":
                id = Integer.parseInt(tokens[2]);
                String field = tokens[3];
                String value = tokens[4];
                productController.handleUpdate(id, field, value);
                break;
            case "remove":
                id = Integer.parseInt(tokens[2]);
                productController.handleRemove(id);
                break;
            case default:
                view.showMessage("Unknown command: " + String.join(" ", Arrays.copyOfRange(tokens, 0, tokens.length)));
                break;
        }
    }

    private void handleTicket(String[] tokens) {
        switch (tokens[1].toLowerCase()) {
            case "new":
                ticketController.handleNew();
                break;
            case "add":
                int id= Integer.parseInt(tokens[2]);
                int quantity = Integer.parseInt(tokens[3]);
                ticketController.handleAdd(id, quantity);
                break;
            case "remove":
                id = Integer.parseInt(tokens[2]);
                ticketController.handleRemove(id);
                break;
            case "print":
                ticketController.handlePrint();
                break;
            case default:
                view.showMessage("Unknown command: " + String.join(" ", Arrays.copyOfRange(tokens, 0, tokens.length)));
                break;
        }
    }

    private void handleEcho(String[] tokens) {
        view.showMessage(String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length)));
    }

}