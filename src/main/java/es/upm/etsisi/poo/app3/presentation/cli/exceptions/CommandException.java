package es.upm.etsisi.poo.app3.presentation.cli.exceptions;

public class CommandException extends RuntimeException {
    private static final String DESCRIPTION = "Command Exception. The command is incorrect";

    public CommandException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
