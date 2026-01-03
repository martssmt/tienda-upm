package es.upm.etsisi.poo.app3.presentation.cli;

import es.upm.etsisi.poo.app3.presentation.view.View;

import java.io.IOException;

public class ErrorHandler {
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
            view.showError("ERROR (" + ioException.getClass().getSimpleName() + ") >>> " + ioException.getMessage());
        }
        view.showClose();
    }
}