package es.upm.etsisi.poo.app3.presentation.cli;

import java.util.List;

/**
 * The {@code Command} interface represents a single executable command
 * in the command-line interface.
 * <p>
 * Implementations of this interface define the command name, expected
 * parameters, help information, parameter validation, and execution logic.
 * </p>
 *
 * <p>
 * Commands are registered in the {@link CommandLineInterface} and invoked
 * dynamically based on user input.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see CommandLineInterface
 */
public interface Command {

    /**
     * Separator used between command name and parameters.
     */
    String COMMAND_SEPARATOR = " ";

    /**
     * Separator used between individual parameters.
     */
    String PARAM_SEPARATOR = " ";

    /**
     * Returns the command name used to invoke this command.
     *
     * @return the command name
     */
    String name();

    /**
     * Returns the list of expected parameters for this command.
     * <p>
     * Parameters are typically expressed in a user-friendly format
     * (e.g. {@code <id>}, {@code [<optional>]}).
     * </p>
     *
     * @return the list of parameter descriptors
     */
    List<String> params();

    /**
     * Returns a detailed help message describing the command.
     *
     * @return the help message
     */
    String helpMessage();

    /**
     * Validates and processes the provided parameters.
     * <p>
     * Implementations may normalize, reorder, or validate parameters
     * before execution.
     * </p>
     *
     * @param params the raw parameters provided by the user
     * @return the validated or transformed parameters
     */
    String[] assessParams(String[] params);

    /**
     * Executes the command using the provided parameters.
     *
     * @param params the parameters to use for execution
     */
    void execute(String[] params);

    /**
     * Returns a concise help representation of the command.
     * <p>
     * The default implementation prints the command name followed by
     * its expected parameters.
     * </p>
     *
     * @return a short help string for the command
     */
    default String help() {
        StringBuilder result = new StringBuilder(this.name());
        if (!this.params().isEmpty()) {
            result.append(COMMAND_SEPARATOR)
                    .append(String.join(COMMAND_SEPARATOR, this.params()));
        }
        return result.toString();
    }
}