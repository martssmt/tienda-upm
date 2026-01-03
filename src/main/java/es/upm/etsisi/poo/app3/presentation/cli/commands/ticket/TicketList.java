package es.upm.etsisi.poo.app3.presentation.cli.commands.ticket;

import es.upm.etsisi.poo.app3.services.CashierService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

public class TicketList implements Command {

    private final CashierService cashierService;
    private final View view;

    public TicketList(View view, CashierService cashierService) {
        this.cashierService = cashierService;
        this.view = view;
    }

    @Override
    public String name() {
        return "ticket list";
    }

    @Override
    public List<String> params() {
        return List.of();
    }

    @Override
    public String helpMessage() {
        return "Lists all tickets registered in the system.";
    }

    @Override
    public String[] assessParams(String[] params) {
        if (params.length > 0) {
            throw new CommandException("Usage: " + this.help());
        }
        return params;
    }

    @Override
    public void execute(String[] params) {
        List<String> tickets = this.cashierService.ticketList();
        this.view.showList("Tickets:", tickets);
        this.view.show("ticket list: ok");
    }
}