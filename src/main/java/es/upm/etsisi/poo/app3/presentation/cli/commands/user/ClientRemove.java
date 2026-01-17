package es.upm.etsisi.poo.app3.presentation.cli.commands.user;

import es.upm.etsisi.poo.app3.data.model.user.Client;
import es.upm.etsisi.poo.app3.services.ClientService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

/**
 * Command that removes a client from the system.
 * <p>
 * This command deletes an existing {@link Client} identified by its unique
 * identifier (DNI or NIF). Once removed, the client is no longer available
 * in the system.
 * </p>
 *
 * <p>
 * The command expects exactly one parameter: the identifier of the client
 * to be removed.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see ClientService
 * @see Client
 */
public class ClientRemove implements Command {

    /**
     * Service used to manage client persistence and business rules.
     */
    private final ClientService clientService;

    /**
     * View used to display feedback messages.
     */
    private final View view;

    /**
     * Creates a new {@code client remove} command.
     *
     * @param view          the view used to display output
     * @param clientService the service used to manage clients
     */
    public ClientRemove(View view, ClientService clientService) {
        this.clientService = clientService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "client remove"}
     */
    @Override
    public String name() {
        return "client remove";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return a list containing the client identifier
     */
    @Override
    public List<String> params() {
        return List.of("<ID>");
    }

    /**
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Deletes a client by their id.";
    }

    /**
     * Validates the provided parameters.
     *
     * @param params the parameters provided by the user
     * @return the validated parameters
     * @throws CommandException if the number of parameters is not exactly one
     */
    @Override
    public String[] assessParams(String[] params) {
        if (params.length != 1) {
            throw new CommandException("Usage: " + this.help());
        }
        return params;
    }

    /**
     * Executes the command by removing the specified client.
     *
     * @param params the command parameters
     */
    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);

        String id = params[0];
        this.clientService.remove(id);

        this.view.show("client remove: ok");
    }
}