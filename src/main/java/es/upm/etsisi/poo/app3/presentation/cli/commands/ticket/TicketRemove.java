package es.upm.etsisi.poo.app3.presentation.cli.commands.ticket;

import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.services.CashierService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

public class TicketRemove implements Command {

    private final CashierService cashierService;
    private final View view;

    public TicketRemove(View view, CashierService cashierService) {
        this.cashierService = cashierService;
        this.view = view;
    }

    @Override
    public String name() {
        return "ticket remove";
    }

    @Override
    public List<String> params() {
        return List.of("<ticketId>", "<cashId>", "<prodId>");
    }

    @Override
    public String helpMessage() {
        return "Deletes a product from the specified ticket using ticketId, cashId, and prodId.";
    }

    @Override
    public String[] assessParams(String[] params) {
        if (params.length != 3)
            throw new CommandException("Usage: " + this.help());
        return params;
    }

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