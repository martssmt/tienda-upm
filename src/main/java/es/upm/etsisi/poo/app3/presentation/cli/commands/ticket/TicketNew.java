package es.upm.etsisi.poo.app3.presentation.cli.commands.ticket;

import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.services.CashierService;
import es.upm.etsisi.poo.app3.services.ClientService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

public class TicketNew implements Command {

    private final CashierService cashierService;
    private final ClientService clientService;
    private final View view;

    public TicketNew(View view, CashierService cashierService, ClientService clientService) {
        this.cashierService = cashierService;
        this.clientService = clientService;
        this.view = view;
    }

    @Override
    public String name() {
        return "ticket new";
    }

    @Override
    public List<String> params() {
        return List.of("[<id>]", "<cashId>", "<userId>", "-[c|p|s] (default -p option)");
    }

    @Override
    public String helpMessage() {
        return "Creates a new ticket with optional id, cashId, userId and an optional ticket type (-c, -p or -s). If no type is given, -p is used.";
    }

    @Override
    public String[] assessParams(String[] params) {
        if (params.length < 2 || params.length > 4)
            throw new CommandException("Usage: " + this.help());
        // Id
        int index = 0;
        String id = null;
        if (!params[0].startsWith("UW")) {
            if (params.length != 3)
                throw new CommandException("Usage: " + this.help());
            id = params[0];
            index++;
        }
        // CashId + UserId
        String cashId = params[index];
        index++;
        String clientId = params[index];
        if (this.clientService.findById(clientId) == null) {
            throw new CommandException("Client with id " + clientId + " not found.");
        }

        // TicketType -c | -p | -s (default -p)
        String type = "-p";
        if (index < params.length) {
            String t = params[index];
            if (!t.matches("-[cps]")) {
                throw new CommandException("Ticket type " + t + " is invalid. Use -c, -p or -s.");
            }
            type = t;
        }
        return new String[]{id, cashId, clientId, type};
    }

    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);
        String id = params[0];
        String cashId = params[1];
        String clientId = params[2];
        String type = params[3];
        Ticket ticket;
        if (id != null) {
            ticket = new Ticket(id, clientId, cashId, type);
        } else {
            ticket = new Ticket(clientId, cashId, type);
        }
        this.cashierService.newTicket(ticket, cashId);
        this.view.showEntity(ticket);
        this.view.show("ticket new: ok");
    }
}