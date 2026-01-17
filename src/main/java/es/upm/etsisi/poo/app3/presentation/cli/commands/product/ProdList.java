package es.upm.etsisi.poo.app3.presentation.cli.commands.product;

import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;
import es.upm.etsisi.poo.app3.services.PurchasableService;

import java.util.List;

/**
 * Command that lists all purchasable elements stored in the catalog.
 * <p>
 * This command retrieves the full catalog through {@link PurchasableService}
 * and displays it using the {@link View}. The list is shown in a human-readable
 * format relying on each element's {@code toString()} implementation.
 * </p>
 *
 * <p>
 * This command does not accept parameters. If parameters are provided, a
 * {@link CommandException} is thrown.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see PurchasableService
 * @see Purchasable
 * @see View
 */
public class ProdList implements Command {

    /**
     * Service used to access the catalog of purchasable elements.
     */
    private final PurchasableService purchasableService;

    /**
     * View used to display the catalog.
     */
    private final View view;

    /**
     * Creates a new {@code prod list} command.
     *
     * @param view               the view used to display output
     * @param purchasableService the service used to list purchasables
     */
    public ProdList(View view, PurchasableService purchasableService) {
        this.purchasableService = purchasableService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "prod list"}
     */
    @Override
    public String name() {
        return "prod list";
    }

    /**
     * Returns the list of expected parameters.
     * <p>
     * This command does not accept parameters.
     * </p>
     *
     * @return an empty list
     */
    @Override
    public List<String> params() {
        return List.of();
    }

    /**
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Lists all registered products with their id, name, price and stock.";
    }

    /**
     * Validates the provided parameters.
     *
     * @param params the parameters provided by the user
     * @return the validated parameters
     * @throws CommandException if any parameters are provided
     */
    @Override
    public String[] assessParams(String[] params) {
        if (params.length > 0) {
            throw new CommandException("Usage: " + this.help());
        }
        return params;
    }

    /**
     * Executes the command by listing all purchasable elements in the catalog.
     *
     * @param params the command parameters (must be empty)
     */
    @Override
    public void execute(String[] params) {
        this.assessParams(params);

        List<Purchasable<Object>> purchasables = this.purchasableService.list();
        this.view.showList("Catalog:", purchasables);
        this.view.show("prod list: ok");
    }
}