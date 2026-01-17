package es.upm.etsisi.poo.app3.presentation.cli.commands;

import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.CommandLineInterface;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;

import java.util.List;

/**
 * Command that displays help information for all available CLI commands.
 * <p>
 * This command delegates to the {@link CommandLineInterface} to print the
 * list of registered commands along with their usage information.
 * </p>
 *
 * <p>
 * It does not accept any parameters. If parameters are provided, a
 * {@link CommandException} is thrown.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see CommandLineInterface
 */
public class Help implements Command {

    /**
     * Reference to the command line interface.
     */
    private final CommandLineInterface cli;

    /**
     * Creates a new help command.
     *
     * @param cli the command line interface used to display help information
     */
    public Help(CommandLineInterface cli) {
        this.cli = cli;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "help"}
     */
    @Override
    public String name() {
        return "help";
    }

    /**
     * Returns the list of expected parameters.
     * <p>
     * This command does not accept parameters.
     * </p>
     *
     * @return an empty list
     */
    @Override
    public List<String> params() {
        return List.of();
    }

    /**
     * Returns the detailed help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Shows the list of commands available.";
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
     * Executes the help command.
     * <p>
     * This method prints the list of registered commands using the
     * {@link CommandLineInterface}.
     * </p>
     *
     * @param params the command parameters (ignored)
     */
    @Override
    public void execute(String[] params) {
        this.cli.help();
    }
}