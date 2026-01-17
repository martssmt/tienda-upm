package es.upm.etsisi.poo.app3.presentation.cli.commands.user;

import es.upm.etsisi.poo.app3.data.model.user.Client;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;
import es.upm.etsisi.poo.app3.services.ClientService;

import java.util.List;

/**
 * Command that registers a new client in the system.
 * <p>
 * This command creates a {@link Client} using the provided name, identifier
 * (DNI or NIF), email address, and the identifier of the cashier associated
 * with the client.
 * </p>
 *
 * <p>
 * The registration logic is delegated to {@link ClientService}, which applies
 * the corresponding business rules and validations.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see ClientService
 * @see Client
 */
public class ClientAdd implements Command {

    /**
     * Service used to manage client persistence and business rules.
     */
    private final ClientService clientService;

    /**
     * View used to display feedback and results.
     */
    private final View view;

    /**
     * Creates a new {@code client add} command.
     *
     * @param view          the view used to display output
     * @param clientService the service used to register clients
     */
    public ClientAdd(View view, ClientService clientService) {
        this.clientService = clientService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "client add"}
     */
    @Override
    public String name() {
        return "client add";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return parameter descriptors for the command
     */
    @Override
    public List<String> params() {
        return List.of("\"<nombre>\"", "(<DNI>|<NIF>)", "<email>", "<cashId>");
    }

    /**
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Implements a new client with name, DNI or NIF, email and cashId.";
    }

    /**
     * Validates the provided parameters.
     *
     * @param params the parameters provided by the user
     * @return the validated parameters
     * @throws CommandException if the number of parameters is not exactly four
     */
    @Override
    public String[] assessParams(String[] params) {
        if (params.length != 4) {
            throw new CommandException("Usage: " + this.help());
        }
        return params;
    }

    /**
     * Executes the command by creating and registering a new client.
     *
     * @param params the command parameters
     */
    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);

        String name = params[0];
        String id = params[1];
        String mail = params[2];
        String cashId = params[3];

        Client client = new Client(name, mail, cashId);
        this.clientService.add(client, id);

        this.view.showEntity(client);
        this.view.show("client add: ok");
    }
}