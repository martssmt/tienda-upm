package es.upm.etsisi.poo.app3.presentation.cli;

import java.util.List;

public interface Command {
    String COMMAND_SEPARATOR = " ";
    String PARAM_SEPARATOR = " ";

    String name();

    List<String> params();

    String helpMessage();

    String[] assessParams(String[] params);

    void execute(String[] params);

    default String help() {
        StringBuilder result = new StringBuilder(this.name());
        if (!this.params().isEmpty()) {
            result.append(COMMAND_SEPARATOR).append(String.join(COMMAND_SEPARATOR, this.params()));
        }
        return result.toString();
    }
}