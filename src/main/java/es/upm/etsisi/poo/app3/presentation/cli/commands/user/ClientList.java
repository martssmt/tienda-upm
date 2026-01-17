package es.upm.etsisi.poo.app3.presentation.cli.commands.user;

import es.upm.etsisi.poo.app3.data.model.user.Client;
import es.upm.etsisi.poo.app3.services.ClientService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

/**
 * Command that lists all registered clients in the system.
 * <p>
 * This command retrieves every {@link Client} stored in the application
 * through {@link ClientService} and displays their identifier (DNI or NIF),
 * name, email address, and associated cashier identifier using the {@link View}.
 * </p>
 *
 * <p>
 * The command does not accept any parameters.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see ClientService
 * @see Client
 * @see View
 */
public class ClientList implements Command {

    /**
     * Service used to retrieve client information.
     */
    private final ClientService clientService;

    /**
     * View used to display the list of clients.
     */
    private final View view;

    /**
     * Creates a new {@code client list} command.
     *
     * @param view          the view used to display output
     * @param clientService the service used to retrieve clients
     */
    public ClientList(View view, ClientService clientService) {
        this.clientService = clientService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "client list"}
     */
    @Override
    public String name() {
        return "client list";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return an empty list, as this command does not accept parameters
     */
    @Override
    public List<String> params() {
        return List.of();
    }

    /**
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Lists all registered clients with their id or nif, name, email and associated cashier id.";
    }

    /**
     * Validates the provided parameters.
     *
     * @param params the parameters provided by the user
     * @return the validated parameters
     * @throws CommandException if any parameters are provided
     */
    @Override
    public String[] assessParams(String[] params) {
        if (params.length > 0) {
            throw new CommandException("Usage: " + this.help());
        }
        return params;
    }

    /**
     * Executes the command by listing all registered clients.
     *
     * @param params the command parameters (must be empty)
     */
    @Override
    public void execute(String[] params) {
        this.assessParams(params);

        List<Client> clients = this.clientService.list();
        this.view.showList("Client:", clients);
        this.view.show("client list: ok");
    }
}