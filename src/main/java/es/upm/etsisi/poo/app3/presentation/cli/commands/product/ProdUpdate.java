package es.upm.etsisi.poo.app3.presentation.cli.commands.product;

import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.services.PurchasableService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

/**
 * Command that updates an existing product in the catalog.
 * <p>
 * This command allows modifying specific attributes of a product identified
 * by its id. The supported fields that can be updated are:
 * <ul>
 *   <li>{@code NAME}</li>
 *   <li>{@code CATEGORY}</li>
 *   <li>{@code PRICE}</li>
 * </ul>
 * </p>
 *
 * <p>
 * The update operation is delegated to {@link PurchasableService}, which
 * enforces the corresponding business rules.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see PurchasableService
 * @see Product
 */
public class ProdUpdate implements Command {

    /**
     * Service used to manage purchasable persistence and business rules.
     */
    private final PurchasableService purchasableService;

    /**
     * View used to display feedback and results.
     */
    private final View view;

    /**
     * Creates a new {@code prod update} command.
     *
     * @param view               the view used to display output
     * @param purchasableService the service used to update products
     */
    public ProdUpdate(View view, PurchasableService purchasableService) {
        this.purchasableService = purchasableService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "prod update"}
     */
    @Override
    public String name() {
        return "prod update";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return parameter descriptors for the command
     */
    @Override
    public List<String> params() {
        return List.of("<id>", "NAME|CATEGORY|PRICE", "<value>");
    }

    /**
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Updates an existing product by its id, modifying name, price or stock.";
    }

    /**
     * Validates and normalizes the provided parameters.
     *
     * @param params raw parameters provided by the user
     * @return normalized parameters in the form {@code [id, field, value]}
     * @throws CommandException if the parameters do not match the expected format
     */
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
                        && !params[2].equals("ELECTRONICS")) {
                    throw new CommandException("Category must be a valid one");
                }
                yield params[2];
            }

            case "PRICE" -> {
                if (!params[2].matches("[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?")) {
                    throw new CommandException("Price must be a valid number");
                }
                yield params[2];
            }

            default -> "";
        };

        return new String[]{id, field, value};
    }

    /**
     * Executes the command by updating the specified product.
     *
     * @param params the command parameters
     */
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