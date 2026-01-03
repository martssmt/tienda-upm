package es.upm.etsisi.poo.app3.presentation.cli.commands.user;

import es.upm.etsisi.poo.app3.data.model.user.Cashier;
import es.upm.etsisi.poo.app3.services.CashierService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

public class CashRemove implements Command {

    private final CashierService cashierService;
    private final View view;

    public CashRemove(View view, CashierService cashierService) {
        this.cashierService = cashierService;
        this.view = view;
    }

    @Override
    public String name() {
        return "cash remove";
    }

    @Override
    public List<String> params() {
        return List.of("<id>");
    }

    @Override
    public String helpMessage() {
        return "Deletes a cashier register with the specified id.";
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
        Cashier cashier = this.cashierService.remove(id);
        this.view.showEntity(cashier);
        this.view.show("cash remove: ok");
    }
}