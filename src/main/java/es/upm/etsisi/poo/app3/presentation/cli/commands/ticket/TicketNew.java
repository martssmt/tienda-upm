package es.upm.etsisi.poo.app3.presentation.cli.commands.ticket;

import es.upm.etsisi.poo.app3.data.model.shop.TicketType;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.data.model.user.ClientType;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;
import es.upm.etsisi.poo.app3.services.CashierService;
import es.upm.etsisi.poo.app3.services.ClientService;

import java.util.List;

/**
 * Command that creates a new ticket in the system.
 * <p>
 * This command creates a {@link Ticket} associated with a cashier and a client.
 * An optional ticket identifier can be provided. The ticket type can also be
 * specified using one of the following flags:
 * <ul>
 *   <li>{@code -p} Product ticket (default)</li>
 *   <li>{@code -s} Service ticket</li>
 *   <li>{@code -c} Combined ticket</li>
 * </ul>
 * </p>
 *
 * <p>
 * The client type is automatically determined from the client identifier and
 * validated according to the ticket type rules.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see Ticket
 * @see TicketType
 * @see CashierService
 * @see ClientService
 */
public class TicketNew implements Command {

    /**
     * Service used to manage cashier operations and ticket creation.
     */
    private final CashierService cashierService;

    /**
     * Service used to retrieve client information.
     */
    private final ClientService clientService;

    /**
     * View used to display feedback and results.
     */
    private final View view;

    /**
     * Creates a new {@code ticket new} command.
     *
     * @param view           the view used to display output
     * @param cashierService the service used to manage tickets
     * @param clientService  the service used to retrieve clients
     */
    public TicketNew(View view, CashierService cashierService, ClientService clientService) {
        this.cashierService = cashierService;
        this.clientService = clientService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "ticket new"}
     */
    @Override
    public String name() {
        return "ticket new";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return parameter descriptors for the command
     */
    @Override
    public List<String> params() {
        return List.of("[<id>]", "<cashId>", "<userId>", "-[c|p|s] (default -p option)");
    }

    /**
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Creates a new ticket with optional id, cashId, userId and an optional ticket type (-c, -p or -s). If no type is given, -p is used.";
    }

    /**
     * Validates and normalizes the provided parameters.
     * <p>
     * The returned array is normalized as:
     * {@code [ticketId, cashId, clientId, typeFlag]}.
     * </p>
     *
     * @param params raw parameters provided by the user
     * @return normalized parameters
     * @throws CommandException if the parameters are invalid or inconsistent
     */
    @Override
    public String[] assessParams(String[] params) {
        if (params.length < 2 || params.length > 4) {
            throw new CommandException("Usage: " + this.help());
        }

        int index = 0;
        String id = null;

        // Optional ticket id
        if (!params[0].startsWith("UW")) {
            if (params.length != 3 && params.length != 4) {
                throw new CommandException("Usage: " + this.help());
            }
            id = params[0];
            index++;
        }

        // Cashier and client identifiers
        String cashId = params[index];
        index++;
        String clientId = params[index];

        if (this.clientService.findById(clientId) == null) {
            throw new CommandException("Client with id " + clientId + " not found.");
        }

        // Ticket type (-p by default)
        String type = "-p";
        index++;
        if (index < params.length) {
            String t = params[index];
            if (!t.matches("-[cps]")) {
                throw new CommandException("Ticket type " + t + " is invalid. Use -c, -p or -s.");
            }
            type = t;
        }

        return new String[]{id, cashId, clientId, type};
    }

    /**
     * Executes the command by creating and registering a new ticket.
     *
     * @param params the command parameters
     */
    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);

        String id = params[0];
        String cashId = params[1];
        String clientId = params[2];
        String type = params[3];

        TicketType ticketType = switch (type) {
            case "-c" -> TicketType.COMBINED;
            case "-s" -> TicketType.SERVICE;
            default -> TicketType.PRODUCT;
        };

        ClientType clientType = this.clientService.findById(clientId).getClientType();

        Ticket ticket;
        if (id != null) {
            ticket = new Ticket(id, ticketType, clientType);
        } else {
            ticket = new Ticket(ticketType, clientType);
        }

        this.cashierService.newTicket(ticket, cashId, clientId);
        this.view.showEntity(ticket);
        this.view.show("ticket new: ok");
    }
}