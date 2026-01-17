package es.upm.etsisi.poo.app3.presentation.cli.commands;

import es.upm.etsisi.poo.app3.presentation.cli.Command;

import java.util.List;

import static es.upm.etsisi.poo.app3.presentation.cli.CommandLineInterface.EXIT;

/**
 * Command that terminates the command-line application.
 * <p>
 * This command represents the logical exit operation of the CLI. The actual
 * termination of the execution loop is handled directly by the
 * {@link es.upm.etsisi.poo.app3.presentation.cli.CommandLineInterface}, so this
 * command is never executed in the usual way.
 * </p>
 *
 * <p>
 * As a result, parameter validation and execution methods are not supported
 * and will throw {@link UnsupportedOperationException} if invoked.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 */
public class Exit implements Command {

    /**
     * Returns the command name.
     *
     * @return the string {@code "exit"}
     */
    @Override
    public String name() {
        return EXIT;
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
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Closes the application.";
    }

    /**
     * Parameter validation is not supported for this command.
     *
     * @param params the parameters provided by the user
     * @throws UnsupportedOperationException always
     */
    @Override
    public String[] assessParams(String[] params) {
        throw new UnsupportedOperationException("Not supported here");
    }

    /**
     * Execution is not supported for this command.
     * <p>
     * The CLI handles exit logic directly without invoking this method.
     * </p>
     *
     * @param params the parameters (ignored)
     * @throws UnsupportedOperationException always
     */
    @Override
    public void execute(String[] params) {
        throw new UnsupportedOperationException("Not supported here");
    }
}