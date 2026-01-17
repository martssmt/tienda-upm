package es.upm.etsisi.poo.app3.presentation.cli.commands.product;

import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;
import es.upm.etsisi.poo.app3.services.PurchasableService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

/**
 * Command that removes a purchasable element from the catalog by its identifier.
 * <p>
 * This command deletes an existing product or service from the catalog using
 * its unique identifier. The removed element is displayed after successful
 * deletion.
 * </p>
 *
 * <p>
 * The command expects exactly one parameter: the identifier of the purchasable
 * element to remove.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see PurchasableService
 * @see Purchasable
 * @see View
 */
public class ProdRemove implements Command {

    /**
     * Service used to manage purchasable persistence and business rules.
     */
    private final PurchasableService purchasableService;

    /**
     * View used to display feedback and results.
     */
    private final View view;

    /**
     * Creates a new {@code prod remove} command.
     *
     * @param view               the view used to display output
     * @param purchasableService the service used to remove purchasables
     */
    public ProdRemove(View view, PurchasableService purchasableService) {
        this.purchasableService = purchasableService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "prod remove"}
     */
    @Override
    public String name() {
        return "prod remove";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return a list containing the product or service identifier
     */
    @Override
    public List<String> params() {
        return List.of("<id>");
    }

    /**
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Deletes an existing product by its id.";
    }

    /**
     * Validates the provided parameters.
     *
     * @param params the parameters provided by the user
     * @return the validated parameters
     * @throws CommandException if the number of parameters is not exactly one
     */
    @Override
    public String[] assessParams(String[] params) {
        if (params.length != 1) {
            throw new CommandException("Usage: " + this.help());
        }
        return params;
    }

    /**
     * Executes the command by removing the specified purchasable element.
     *
     * @param params the command parameters
     */
    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);

        String id = params[0];
        Purchasable purchasable = this.purchasableService.remove(id);

        this.view.showEntity(purchasable);
        this.view.show("prod remove: ok");
    }
}