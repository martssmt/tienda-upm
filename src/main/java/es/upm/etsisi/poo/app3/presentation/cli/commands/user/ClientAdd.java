package es.upm.etsisi.poo.app3.presentation.cli.commands.user;

import es.upm.etsisi.poo.app3.data.model.user.Client;
import es.upm.etsisi.poo.app3.services.ClientService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

public class ClientAdd implements Command {

    private final ClientService clientService;
    private final View view;

    public ClientAdd(View view, ClientService clientService) {
        this.clientService = clientService;
        this.view = view;
    }

    @Override
    public String name() {
        return "client add";
    }

    @Override
    public List<String> params() {
        return List.of("\"<nombre>\"", "(<DNI>|<NIF>)", "<email>", "<cashId>");
    }

    @Override
    public String helpMessage() {
        return "Implements a new client with name, DNI or NIF, email and cashId.";
    }

    @Override
    public String[] assessParams(String[] params) {
        if (params.length != 4)
            throw new CommandException("Usage: " + this.help());
        return params;

    }

    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);
        String name = params[0];
        String dni = params[1];
        String mail = params[2];
        String cashId = params[3];
        Client client = new Client(name, mail, cashId);
        this.clientService.add(client, dni);
        this.view.showEntity(client);
        this.view.show("client add: ok");
    }
}