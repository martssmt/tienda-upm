package es.upm.etsisi.poo.app3.presentation.cli.commands;

import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.CommandLineInterface;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;

import java.util.List;

public class Help implements Command {

    CommandLineInterface cli;

    public Help(CommandLineInterface cli) {
        this.cli = cli;
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public List<String> params() {
        return List.of();
    }

    @Override
    public String helpMessage() {
        return "Shows the list of commands available.";
    }

    @Override
    public String[] assessParams(String[] params) {
        if (params.length > 0) {
            throw new CommandException("Usage: " + this.help());
        }
        return params;
    }

    @Override
    public void execute(String[] params) {
        //params = this.assessParams(params);
        this.cli.help();
    }
}