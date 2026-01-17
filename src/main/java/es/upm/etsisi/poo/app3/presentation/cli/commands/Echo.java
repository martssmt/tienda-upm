package es.upm.etsisi.poo.app3.presentation.cli.commands;

import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

/**
 * Command that prints the provided text to the output.
 * <p>
 * This command echoes back the text passed as parameter, preserving spaces
 * and enclosing the output in quotation marks. It is mainly intended for
 * testing parameter parsing and command execution within the CLI.
 * </p>
 *
 * <p>
 * The command expects a single text parameter, which may contain spaces.
 * If the parameters do not match the expected format, a
 * {@link CommandException} is thrown.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see View
 */
public class Echo implements Command {

    /**
     * View used to display the echoed text.
     */
    private final View view;

    /**
     * Creates a new echo command.
     *
     * @param view the view used to display output
     */
    public Echo(View view) {
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "echo"}
     */
    @Override
    public String name() {
        return "echo";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return a list containing a single text parameter descriptor
     */
    @Override
    public List<String> params() {
        return List.of("\"<text>\"");
    }

    /**
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Prints the provided text.";
    }

    /**
     * Validates the provided parameters.
     *
     * @param params the parameters provided by the user
     * @return the validated parameters
     * @throws CommandException if the parameters do not match the expected format
     */
    @Override
    public String[] assessParams(String[] params) {
        if (params.length == 0
                || (params[0].startsWith("\"") && params[params.length - 1].endsWith("\""))) {
            throw new CommandException("Usage: " + this.help());
        }
        return params;
    }

    /**
     * Executes the echo command.
     * <p>
     * The provided parameters are joined and printed, enclosed in
     * quotation marks.
     * </p>
     *
     * @param params the parameters to echo
     */
    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);

        if (params.length == 0) {
            this.view.show("\"\"");
        } else {
            String text = String.join(PARAM_SEPARATOR, params);
            this.view.show("\"" + text + "\"");
        }
    }
}