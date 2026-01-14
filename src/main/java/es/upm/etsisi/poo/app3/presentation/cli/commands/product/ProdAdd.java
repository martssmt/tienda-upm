package es.upm.etsisi.poo.app3.presentation.cli.commands.product;

import es.upm.etsisi.poo.app3.data.model.shop.Category;
import es.upm.etsisi.poo.app3.data.model.shop.ServiceType;
import es.upm.etsisi.poo.app3.data.model.shop.products.*;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;
import es.upm.etsisi.poo.app3.services.PurchasableService;

import java.time.LocalDate;
import java.util.List;

public class ProdAdd implements Command {

    private final PurchasableService purchasableService;
    private final View view;

    public ProdAdd(View view, PurchasableService purchasableService) {
        this.purchasableService = purchasableService;
        this.view = view;
    }

    @Override
    public String name() {
        return "prod add";
    }

    @Override
    public List<String> params() {
        return List.of("PRODUCTO: [<id>] \"<name>\" <category> <price> [<maxTexts>]",
                "SERVICIO: <expiration: yyyy-MM-dd> <serviceType>");
    }

    @Override
    public String helpMessage() {
        return "Adds a new product or service to the catalog..";
    }

    @Override
    public String[] assessParams(String[] params) {
        if (params == null || params.length < 2 || params.length > 5)
            throw new CommandException("Usage: " + this.help());

        // -- SERVICIO (2 parámetros) --
        if (params.length == 2) {
            String expiration = params[0];
            String type = params[1];
            // fecha
            if (!expiration.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new CommandException("Invalid date format. Use yyyy-MM-dd");
            }
            // ServiceType
            if (!type.equals("INSURANCE") && !type.equals("TRANSPORT") && !type.equals("SHOW")) {
                throw new CommandException("Invalid service type.");
            }
            // Return
            return new String[]{null, expiration, type, null, null};
        }

        // -- PRODUCTO (3 a 5 parámetros) --
        int index = 0;
        // Id
        String id = null;
        if (params[0].matches("-?\\d+") && params.length >= 4) {
            id = params[0];
            index++;
        } else {
            if (params.length > 4)
                throw new CommandException("Usage: " + this.help());
        }
        // Name
        String name = params[index];
        if (name.isEmpty())
            throw new CommandException("Usage: " + this.help());
        index++;
        // Category
        String category = params[index];
        if (!category.equals("MERCH") && !category.equals("STATIONERY")
                && !category.equals("CLOTHES") && !category.equals("BOOK")
                && !category.equals("ELECTRONICS"))
            throw new CommandException("Usage: " + this.help());
        index++;
        // Price
        String price = params[index];
        if (!price.matches("[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?")) {
            throw new CommandException("Usage: " + this.help());
        }
        index++;
        // maxPers (optional)
        String maxPers = null;
        if (index < params.length) {
            maxPers = params[index];
            if (!maxPers.matches("-?\\d+"))
                throw new CommandException("Usage: " + this.help());
        }
        return new String[]{id, name.trim(), category, price, maxPers};
    }

    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);

        Purchasable<?> purchasable;

        if (params[3] == null) {
            // -- SERVICIO --
            LocalDate expiration = LocalDate.parse(params[1]);
            ServiceType type = ServiceType.valueOf(params[2].toUpperCase());
            purchasable = new ServiceProduct(expiration, type);
            this.purchasableService.add(purchasable);
        } else {
            // -- PRODUCTO --
            String id = params[0];
            String name = params[1];
            Category category = Category.valueOf(params[2]);
            Double price = Double.parseDouble(params[3]);
            Integer numberTexts = null;
            if (params[4] != null) {
                numberTexts = Integer.parseInt(params[4]);
            }
            if (numberTexts == null) {
                purchasable = new BasicProduct(name, category, price);
            } else {
                purchasable = new CustomProduct(name, category, price, numberTexts);
            }
            if (id == null) {
                this.purchasableService.add(purchasable);
            } else {
                this.purchasableService.add(purchasable, id);
            }
        }
        this.view.showEntity(purchasable);
        this.view.show("prod add: ok");
    }
}