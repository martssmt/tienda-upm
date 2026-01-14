package es.upm.etsisi.poo.app3.presentation.cli.commands.user;

import es.upm.etsisi.poo.app3.data.model.user.Client;
import es.upm.etsisi.poo.app3.services.ClientService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

public class ClientRemove implements Command {

    private final ClientService clientService;
    private final View view;

    public ClientRemove(View view, ClientService clientService) {
        this.clientService = clientService;
        this.view = view;
    }

    @Override
    public String name() {
        return "client remove";
    }

    @Override
    public List<String> params() {
        return List.of("<ID>");
    }

    @Override
    public String helpMessage() {
        return "Deletes a client by their id.";
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
        Client client = this.clientService.remove(id);
        this.view.showEntity(client);
        this.view.show("client remove: ok");
    }
}