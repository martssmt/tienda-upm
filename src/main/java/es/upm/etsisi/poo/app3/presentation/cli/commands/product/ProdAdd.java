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

/**
 * Command that adds a new purchasable element (product or service) to the catalog.
 * <p>
 * This command supports two input modes:
 * <ul>
 *   <li><b>Service mode (2 params):</b> {@code <expiration: yyyy-MM-dd> <serviceType>}</li>
 *   <li><b>Product mode (3 to 5 params):</b>
 *   {@code [<id>] "<name>" <category> <price> [<maxTexts>]}</li>
 * </ul>
 * </p>
 *
 * <p>
 * In product mode, the command creates either a {@link BasicProduct} or a
 * {@link CustomProduct} depending on whether {@code maxTexts} is provided.
 * In service mode, it creates a {@link ServiceProduct} with an expiration date.
 * The created element is persisted through {@link PurchasableService}.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see PurchasableService
 * @see BasicProduct
 * @see CustomProduct
 * @see ServiceProduct
 */
public class ProdAdd implements Command {

    /**
     * Service used to manage purchasable persistence and business rules.
     */
    private final PurchasableService purchasableService;

    /**
     * View used to display feedback and results.
     */
    private final View view;

    /**
     * Creates a new {@code prod add} command.
     *
     * @param view             the view used to display output
     * @param purchasableService the service used to add purchasables
     */
    public ProdAdd(View view, PurchasableService purchasableService) {
        this.purchasableService = purchasableService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "prod add"}
     */
    @Override
    public String name() {
        return "prod add";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return parameter descriptors for product and service modes
     */
    @Override
    public List<String> params() {
        return List.of(
                "PRODUCTO: [<id>] \"<name>\" <category> <price> [<maxTexts>]",
                "SERVICIO: <expiration: yyyy-MM-dd> <serviceType>"
        );
    }

    /**
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Adds a new product or service to the catalog..";
    }

    /**
     * Validates and normalizes the provided parameters.
     * <p>
     * The returned array uses a normalized layout:
     * <ul>
     *   <li>{@code [0]} product id (nullable)</li>
     *   <li>{@code [1]} name (or expiration date for services)</li>
     *   <li>{@code [2]} category (or service type for services)</li>
     *   <li>{@code [3]} price (nullable for services)</li>
     *   <li>{@code [4]} maxTexts (nullable)</li>
     * </ul>
     * </p>
     *
     * @param params raw parameters provided by the user
     * @return normalized parameters
     * @throws CommandException if the parameters do not match the expected format
     */
    @Override
    public String[] assessParams(String[] params) {
        if (params == null || params.length < 2 || params.length > 5) {
            throw new CommandException("Usage: " + this.help());
        }

        // -- SERVICE (2 parameters) --
        if (params.length == 2) {
            String expiration = params[0];
            String type = params[1];

            if (!expiration.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new CommandException("Invalid date format. Use yyyy-MM-dd");
            }

            // Note: validated against expected CLI values
            if (!type.equals("INSURANCE") && !type.equals("TRANSPORT") && !type.equals("SHOW")) {
                throw new CommandException("Invalid service type.");
            }

            return new String[]{null, expiration, type, null, null};
        }

        // -- PRODUCT (3 to 5 parameters) --
        int index = 0;

        // Optional id
        String id = null;
        if (params[0].matches("-?\\d+") && params.length >= 4) {
            id = params[0];
            index++;
        } else {
            if (params.length > 4) {
                throw new CommandException("Usage: " + this.help());
            }
        }

        // Name
        String name = params[index];
        if (name.isEmpty()) {
            throw new CommandException("Usage: " + this.help());
        }
        index++;

        // Category
        String category = params[index];
        if (!category.equals("MERCH") && !category.equals("STATIONERY")
                && !category.equals("CLOTHES") && !category.equals("BOOK")
                && !category.equals("ELECTRONICS")) {
            throw new CommandException("Usage: " + this.help());
        }
        index++;

        // Price
        String price = params[index];
        if (!price.matches("[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?")) {
            throw new CommandException("Usage: " + this.help());
        }
        index++;

        // Optional maxTexts
        String maxPers = null;
        if (index < params.length) {
            maxPers = params[index];
            if (!maxPers.matches("-?\\d+")) {
                throw new CommandException("Usage: " + this.help());
            }
        }

        return new String[]{id, name.trim(), category, price, maxPers};
    }

    /**
     * Executes the command by creating and persisting the corresponding purchasable.
     * <p>
     * If {@code params[3]} is {@code null}, the input is interpreted as a service.
     * Otherwise, it is interpreted as a product.
     * </p>
     *
     * @param params raw parameters provided by the user
     */
    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);

        Purchasable<?> purchasable;

        if (params[3] == null) {
            // -- SERVICE --
            LocalDate expiration = LocalDate.parse(params[1]);
            ServiceType type = ServiceType.valueOf(params[2].toUpperCase());
            purchasable = new ServiceProduct(expiration, type);
            this.purchasableService.add(purchasable);
        } else {
            // -- PRODUCT --
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