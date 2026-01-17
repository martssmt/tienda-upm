package es.upm.etsisi.poo.app3.presentation.cli.commands.user;

import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;
import es.upm.etsisi.poo.app3.services.CashierService;

import java.util.List;

/**
 * Command that lists all tickets created by a specific cashier.
 * <p>
 * This command retrieves every ticket associated with the cashier identified
 * by the given identifier and displays their names and current status using
 * the {@link View}.
 * </p>
 *
 * <p>
 * The command expects exactly one parameter: the identifier of the cashier.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see CashierService
 * @see View
 */
public class CashTickets implements Command {

    /**
     * Service used to access cashier and ticket information.
     */
    private final CashierService cashierService;

    /**
     * View used to display the list of tickets.
     */
    private final View view;

    /**
     * Creates a new {@code cash tickets} command.
     *
     * @param view           the view used to display output
     * @param cashierService the service used to retrieve tickets
     */
    public CashTickets(View view, CashierService cashierService) {
        this.cashierService = cashierService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "cash tickets"}
     */
    @Override
    public String name() {
        return "cash tickets";
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
        return "Shows all tickets created by the cashier with the given id.";
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
     * Executes the command by listing all tickets created by the specified cashier.
     *
     * @param params the command parameters
     */
    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);

        String id = params[0];
        List<String> tickets = this.cashierService.ticketListFromCashier(id);

        this.view.showList("Tickets: ", tickets);
        this.view.show("cash tickets: ok");
    }
}