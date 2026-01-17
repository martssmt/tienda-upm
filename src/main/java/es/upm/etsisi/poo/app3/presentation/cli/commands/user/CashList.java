package es.upm.etsisi.poo.app3.presentation.cli.commands.user;

import es.upm.etsisi.poo.app3.data.model.user.Cashier;
import es.upm.etsisi.poo.app3.services.CashierService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

/**
 * Command that lists all registered cashiers in the system.
 * <p>
 * This command retrieves every {@link Cashier} stored in the application
 * through {@link CashierService} and displays their identifiers, names,
 * and email addresses using the {@link View}.
 * </p>
 *
 * <p>
 * The command does not accept any parameters.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see CashierService
 * @see Cashier
 * @see View
 */
public class CashList implements Command {

    /**
     * Service used to retrieve cashier information.
     */
    private final CashierService cashierService;

    /**
     * View used to display the list of cashiers.
     */
    private final View view;

    /**
     * Creates a new {@code cash list} command.
     *
     * @param view           the view used to display output
     * @param cashierService the service used to retrieve cashiers
     */
    public CashList(View view, CashierService cashierService) {
        this.cashierService = cashierService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "cash list"}
     */
    @Override
    public String name() {
        return "cash list";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return an empty list, as this command does not accept parameters
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
        return "Lists all registered cashiers with their id, name and email.";
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
     * Executes the command by listing all registered cashiers.
     *
     * @param params the command parameters (must be empty)
     */
    @Override
    public void execute(String[] params) {
        this.assessParams(params);

        List<Cashier> cashiers = this.cashierService.list();
        this.view.showList("Cash:", cashiers);
        this.view.show("cash list: ok");
    }
}