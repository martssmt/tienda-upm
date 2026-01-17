package es.upm.etsisi.poo.app3.presentation.cli.commands.ticket;

import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.services.CashierService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

/**
 * Command that prints and closes a ticket.
 * <p>
 * This command finalizes the specified ticket by closing it and then prints
 * its formatted details. The printing behavior depends on the ticket's
 * client type (person or company) and is handled internally by the ticket's
 * printing strategy.
 * </p>
 *
 * <p>
 * The command requires exactly two parameters:
 * <ul>
 *   <li>{@code <ticketId>} – the identifier of the ticket</li>
 *   <li>{@code <cashId>} – the identifier of the cashier managing the ticket</li>
 * </ul>
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see CashierService
 * @see Ticket
 * @see es.upm.etsisi.poo.app3.data.model.shop.ticket.TicketPrintingStrategy
 */
public class TicketPrint implements Command {

    /**
     * Service used to manage cashier operations and ticket printing.
     */
    private final CashierService cashierService;

    /**
     * View used to display the printed ticket.
     */
    private final View view;

    /**
     * Creates a new {@code ticket print} command.
     *
     * @param view           the view used to display output
     * @param cashierService the service used to manage tickets
     */
    public TicketPrint(View view, CashierService cashierService) {
        this.cashierService = cashierService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "ticket print"}
     */
    @Override
    public String name() {
        return "ticket print";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return parameter descriptors for the command
     */
    @Override
    public List<String> params() {
        return List.of("<ticketId>", "<cashId>");
    }

    /**
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Prints the details of a ticket by ticketId.";
    }

    /**
     * Validates the provided parameters.
     *
     * @param params the parameters provided by the user
     * @return the validated parameters
     * @throws CommandException if the number of parameters is not exactly two
     */
    @Override
    public String[] assessParams(String[] params) {
        if (params.length != 2) {
            throw new CommandException("Usage: " + this.help());
        }
        return params;
    }

    /**
     * Executes the command by closing and printing the specified ticket.
     *
     * @param params the command parameters
     */
    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);

        String ticketId = params[0];
        String cashId = params[1];

        Ticket ticket = this.cashierService.print(cashId, ticketId);

        this.view.showEntity(ticket);
        this.view.show("ticket print: ok");
    }
}