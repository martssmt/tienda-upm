package es.upm.etsisi.poo.app3.presentation.cli.commands.product;

import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.services.PurchasableService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

public class ProdUpdate implements Command {

    private final PurchasableService purchasableService;
    private final View view;

    public ProdUpdate(View view, PurchasableService purchasableService) {
        this.purchasableService = purchasableService;
        this.view = view;
    }

    @Override
    public String name() {
        return "prod update";
    }

    @Override
    public List<String> params() {
        return List.of("<id>", "NAME|CATEGORY|PRICE", "<value>");
    }

    @Override
    public String helpMessage() {
        return "Updates an existing product by its id, modifying name, price or stock.";
    }

    @Override
    public String[] assessParams(String[] params) {
        if (params.length < 3) {
            throw new CommandException("Usage: " + this.help());
        }
        // Id
        String id = params[0];
        if (!id.matches("-?\\d+")) {
            throw new CommandException("Usage: " + this.help());
        }
        // Field
        String field = params[1];
        if (!field.equals("NAME") && !field.equals("CATEGORY") && !field.equals("PRICE")) {
            throw new CommandException("Usage: " + this.help());
        }
        // Value
        String value = switch (field) {
            case "NAME" -> params[2].trim();
            case "CATEGORY" -> {
                if (!params[2].equals("MERCH") && !params[2].equals("STATIONARY")
                        && !params[2].equals("CLOTHES") && !params[2].equals("BOOK")
                        && !params[2].equals("ELECTRONICS"))
                    throw new CommandException("Category must be a valid one");
                yield params[2];
            }
            case "PRICE" -> {
                if (!params[2].matches("[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?"))
                    throw new CommandException("Price must be a valid number");
                yield params[2];
            }
            default -> "";
        };
        return new String[]{id, field, value};
    }

    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);
        String id = params[0];
        String field = params[1];
        String value = params[2];
        Product product = this.purchasableService.update(id, field, value);
        this.view.showEntity(product);
        this.view.show("prod update: ok");
    }
}