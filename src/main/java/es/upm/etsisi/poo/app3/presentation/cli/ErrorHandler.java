package es.upm.etsisi.poo.app3.presentation.cli;

import es.upm.etsisi.poo.app3.presentation.view.View;

import java.io.IOException;

/**
 * The {@code ErrorHandler} class centralizes error handling for the
 * command-line interface execution flow.
 * <p>
 * This class is responsible for initializing the application, executing
 * commands either interactively or from a file, catching I/O-related
 * exceptions, and ensuring a clean shutdown message is always displayed.
 * </p>
 *
 * <p>
 * By isolating error-handling logic, this class keeps the {@link CommandLineInterface}
 * focused on command parsing and execution, improving separation of concerns
 * within the presentation layer.
 * </p>
 *
 * @author Tomás
 * @version 3.0
 * @see CommandLineInterface
 * @see View
 */
public class ErrorHandler {

    /**
     * Executes the command-line interface and handles I/O errors gracefully.
     * <p>
     * If no arguments are provided, the application runs in interactive mode.
     * If a file path is provided as the first argument, commands are read from
     * the specified file.
     * </p>
     *
     * @param commandLineInterface the command-line interface to execute
     * @param view                 the view used to display messages and errors
     * @param args                 the application arguments
     */
    public void handlesErrors(CommandLineInterface commandLineInterface,
                              View view, String[] args) {

        view.showInit();
        try {
            if (args.length == 0) {
                commandLineInterface.runCommands();
            } else {
                commandLineInterface.runCommandsFromFile(args[0]);
            }
        } catch (IOException ioException) {
            view.showError(
                    "ERROR (" + ioException.getClass().getSimpleName() + ") >>> "
                            + ioException.getMessage()
            );
        }
        view.showClose();
    }
}