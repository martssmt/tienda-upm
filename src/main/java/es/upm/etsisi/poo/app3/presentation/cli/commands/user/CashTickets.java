package es.upm.etsisi.poo.app3.presentation.cli.commands.user;

import es.upm.etsisi.poo.app3.services.CashierService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

public class CashTickets implements Command {

    private final CashierService cashierService;
    private final View view;

    public CashTickets(View view, CashierService cashierService) {
        this.cashierService = cashierService;
        this.view = view;
    }

    @Override
    public String name() {
        return "cash tickets";
    }

    @Override
    public List<String> params() {
        return List.of("<id>");
    }

    @Override
    public String helpMessage() {
        return "Shows all tickets created by the cashier with the given id.";
    }

    @Override
    public String[] assessParams(String[] params) {
        if (params.length != 1) {
            throw new CommandException("Usage: " + this.help());
        }
        return params;
    }

    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);
        String id = params[0];
        List<String> tickets = this.cashierService.ticketListFromCashier(id);
        this.view.showList("Tickets: ", tickets);
        this.view.show("cash tickets: ok");
    }
}