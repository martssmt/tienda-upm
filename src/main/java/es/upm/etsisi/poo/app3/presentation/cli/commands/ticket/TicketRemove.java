package es.upm.etsisi.poo.app3.presentation.cli.commands.ticket;

import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.services.CashierService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

/**
 * Command that removes a product from an existing ticket.
 * <p>
 * This command deletes a specific product from a ticket managed by a cashier,
 * using the ticket identifier, cashier identifier, and product identifier.
 * After removal, the updated ticket is displayed.
 * </p>
 *
 * <p>
 * The command requires exactly three parameters:
 * <ul>
 *   <li>{@code <ticketId>} – the identifier of the ticket</li>
 *   <li>{@code <cashId>} – the identifier of the cashier</li>
 *   <li>{@code <prodId>} – the identifier of the product to remove</li>
 * </ul>
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see CashierService
 * @see Ticket
 */
public class TicketRemove implements Command {

    /**
     * Service used to manage cashier operations and ticket updates.
     */
    private final CashierService cashierService;

    /**
     * View used to display the updated ticket.
     */
    private final View view;

    /**
     * Creates a new {@code ticket remove} command.
     *
     * @param view           the view used to display output
     * @param cashierService the service used to manage tickets
     */
    public TicketRemove(View view, CashierService cashierService) {
        this.cashierService = cashierService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "ticket remove"}
     */
    @Override
    public String name() {
        return "ticket remove";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return parameter descriptors for the command
     */
    @Override
    public List<String> params() {
        return List.of("<ticketId>", "<cashId>", "<prodId>");
    }

    /**
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Deletes a product from the specified ticket using ticketId, cashId, and prodId.";
    }

    /**
     * Validates the provided parameters.
     *
     * @param params the parameters provided by the user
     * @return the validated parameters
     * @throws CommandException if the number of parameters is not exactly three
     */
    @Override
    public String[] assessParams(String[] params) {
        if (params.length != 3) {
            throw new CommandException("Usage: " + this.help());
        }
        return params;
    }

    /**
     * Executes the command by removing the specified product from the ticket.
     *
     * @param params the command parameters
     */
    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);

        String ticketId = params[0];
        String cashId = params[1];
        String prodId = params[2];

        Ticket ticket = this.cashierService.removeProduct(cashId, ticketId, prodId);

        this.view.showEntity(ticket);
        this.view.show("ticket remove: ok");
    }
}