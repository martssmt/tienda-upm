package es.upm.etsisi.poo.app3.presentation.cli.commands.ticket;

import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;
import es.upm.etsisi.poo.app3.services.CashierService;

import java.util.List;

/**
 * Command that lists all tickets registered in the system.
 * <p>
 * This command retrieves every ticket managed by the application through
 * {@link CashierService} and displays their identifiers and current status
 * using the {@link View}.
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
 * @see View
 */
public class TicketList implements Command {

    /**
     * Service used to access ticket information.
     */
    private final CashierService cashierService;

    /**
     * View used to display the list of tickets.
     */
    private final View view;

    /**
     * Creates a new {@code ticket list} command.
     *
     * @param view           the view used to display output
     * @param cashierService the service used to retrieve tickets
     */
    public TicketList(View view, CashierService cashierService) {
        this.cashierService = cashierService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "ticket list"}
     */
    @Override
    public String name() {
        return "ticket list";
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
        return "Lists all tickets registered in the system.";
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
     * Executes the command by listing all tickets in the system.
     *
     * @param params the command parameters (must be empty)
     */
    @Override
    public void execute(String[] params) {
        List<String> tickets = this.cashierService.ticketList();
        this.view.showList("Tickets:", tickets);
        this.view.show("ticket list: ok");
    }
}