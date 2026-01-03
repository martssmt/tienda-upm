package es.upm.etsisi.poo.app3.presentation.cli.commands.user;

import es.upm.etsisi.poo.app3.data.model.user.Cashier;
import es.upm.etsisi.poo.app3.services.CashierService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

public class CashAdd implements Command {

    private final CashierService cashierService;
    final private View view;

    public CashAdd(View view, CashierService cashierService) {
        this.cashierService = cashierService;
        this.view = view;
    }

    @Override
    public String name() {
        return "cash add";
    }

    @Override
    public List<String> params() {
        return List.of("[<id>]", "\"<nombre>\"", "<email>");
    }

    @Override
    public String helpMessage() {
        return "Implements a new cashier register with optional id, name and email.";
    }

    @Override
    public String[] assessParams(String[] params) {
        if (params == null || params.length < 2 || params.length > 3)
            throw new CommandException("Usage: " + this.help());
        int index = 0;
        // Id
        String id = null;
        if (params[0].matches("UW[0-9]{7}")) {
            if (params.length != 3)
                throw new CommandException("Usage: " + this.help());
            id = params[0];
            index++;
        } else {
            if (params.length != 2)
                throw new CommandException("Usage: " + this.help());
        }
        // Name
        String name = params[index];
        if (name.isEmpty())
            throw new CommandException("Usage: " + this.help());
        index++;
        // Email
        String email = params[index];
        return new String[]{id, name.trim(), email};
    }

    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);
        String id = params[0];
        String name = params[1];
        String mail = params[2];
        Cashier cashier = new Cashier(name, mail);
        if (id == null) {
            this.cashierService.add(cashier);
        } else {
            this.cashierService.add(cashier, id);
        }
        this.view.showEntity(cashier);
        this.view.show("cash add: ok");
    }
}