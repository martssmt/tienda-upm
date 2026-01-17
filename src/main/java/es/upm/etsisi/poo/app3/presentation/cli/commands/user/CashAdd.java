package es.upm.etsisi.poo.app3.presentation.cli.commands.user;

import es.upm.etsisi.poo.app3.data.model.user.Cashier;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;
import es.upm.etsisi.poo.app3.services.CashierService;

import java.util.List;

/**
 * Command that registers a new cashier in the system.
 * <p>
 * This command creates a {@link Cashier} using a name and an email address.
 * An optional cashier identifier can be provided; if omitted, the identifier
 * is generated automatically by the system.
 * </p>
 *
 * <p>
 * The command validates the input parameters and delegates the registration
 * logic to {@link CashierService}.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see CashierService
 * @see Cashier
 */
public class CashAdd implements Command {

    /**
     * Service used to manage cashier persistence and business rules.
     */
    private final CashierService cashierService;

    /**
     * View used to display feedback and results.
     */
    private final View view;

    /**
     * Creates a new {@code cash add} command.
     *
     * @param view           the view used to display output
     * @param cashierService the service used to register cashiers
     */
    public CashAdd(View view, CashierService cashierService) {
        this.cashierService = cashierService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "cash add"}
     */
    @Override
    public String name() {
        return "cash add";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return parameter descriptors for the command
     */
    @Override
    public List<String> params() {
        return List.of("[<id>]", "\"<nombre>\"", "<email>");
    }

    /**
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Implements a new cashier register with optional id, name and email.";
    }

    /**
     * Validates and normalizes the provided parameters.
     * <p>
     * The returned array is normalized as:
     * {@code [id, name, email]}.
     * </p>
     *
     * @param params raw parameters provided by the user
     * @return normalized parameters
     * @throws CommandException if the parameters are invalid or inconsistent
     */
    @Override
    public String[] assessParams(String[] params) {
        if (params == null || params.length < 2 || params.length > 3) {
            throw new CommandException("Usage: " + this.help());
        }

        int index = 0;
        String id = null;

        // Optional cashier id
        if (params[0].matches("UW[0-9]{7}")) {
            if (params.length != 3) {
                throw new CommandException("Usage: " + this.help());
            }
            id = params[0];
            index++;
        } else {
            if (params.length != 2) {
                throw new CommandException("Usage: " + this.help());
            }
        }

        // Name
        String name = params[index];
        if (name.isEmpty()) {
            throw new CommandException("Usage: " + this.help());
        }
        index++;

        // Email
        String email = params[index];

        return new String[]{id, name.trim(), email};
    }

    /**
     * Executes the command by creating and registering a new cashier.
     *
     * @param params the command parameters
     */
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