package es.upm.etsisi.poo.app3.presentation.cli.commands.user;

import es.upm.etsisi.poo.app3.data.model.user.Cashier;
import es.upm.etsisi.poo.app3.services.CashierService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

/**
 * Command that removes a cashier from the system.
 * <p>
 * This command deletes an existing {@link Cashier} identified by its unique
 * identifier. Once removed, the cashier is no longer available in the system.
 * </p>
 *
 * <p>
 * The command expects exactly one parameter: the identifier of the cashier
 * to be removed.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see CashierService
 * @see Cashier
 */
public class CashRemove implements Command {

    /**
     * Service used to manage cashier persistence and business rules.
     */
    private final CashierService cashierService;

    /**
     * View used to display feedback messages.
     */
    private final View view;

    /**
     * Creates a new {@code cash remove} command.
     *
     * @param view           the view used to display output
     * @param cashierService the service used to manage cashiers
     */
    public CashRemove(View view, CashierService cashierService) {
        this.cashierService = cashierService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "cash remove"}
     */
    @Override
    public String name() {
        return "cash remove";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return a list containing the cashier identifier
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
        return "Deletes a cashier register with the specified id.";
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
     * Executes the command by removing the specified cashier.
     *
     * @param params the command parameters
     */
    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);

        String id = params[0];
        this.cashierService.remove(id);

        this.view.show("cash remove: ok");
    }
}