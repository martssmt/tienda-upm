package es.upm.etsisi.poo.app3.presentation.cli;

import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code CommandLineInterface} class provides the interactive command-line
 * entry point for the application.
 * <p>
 * It maintains a registry of available {@link Command} implementations and
 * is responsible for reading user input, resolving the appropriate command,
 * parsing parameters (including quoted parameters), executing commands, and
 * displaying feedback through the {@link View}.
 * </p>
 *
 * <p>
 * The CLI can operate in two modes:
 * <ul>
 *   <li><b>Interactive mode:</b> reads commands from standard input.</li>
 *   <li><b>File mode:</b> reads commands line-by-line from a text file.</li>
 * </ul>
 * </p>
 *
 * <p>
 * A command is matched by finding the longest registered command name that is
 * equal to the input line or is a prefix of it. If the command does not exist,
 * a {@link CommandException} is thrown.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * View view = new View();
 * CommandLineInterface cli = new CommandLineInterface(view);
 * cli.add(new HelpCommand(view, cli));
 * cli.runCommands();
 * }</pre>
 *
 * @author Tomás
 * @version 3.0
 * @see Command
 * @see View
 */
public class CommandLineInterface {

    /**
     * Command name used to terminate the interactive loop.
     */
    public static final String EXIT = "exit";

    /**
     * Registered commands mapped by their name.
     * <p>
     * A {@link LinkedHashMap} is used to preserve insertion order when printing help.
     * </p>
     */
    private final Map<String, Command> commands;

    /**
     * View used to display prompts, output, and errors.
     */
    private final View view;

    /**
     * Creates a new command line interface.
     *
     * @param view the view used to render messages and prompts
     */
    public CommandLineInterface(View view) {
        this.view = view;
        this.commands = new LinkedHashMap<>();
    }

    /**
     * Registers a new command in the CLI.
     *
     * @param command the command to register
     */
    public void add(Command command) {
        this.commands.put(command.name(), command);
    }

    /**
     * Runs the CLI in interactive mode, reading commands from standard input.
     * <p>
     * The loop terminates when the {@link #EXIT} command is executed.
     * Any runtime exceptions during command execution are caught and rendered
     * through the {@link View}.
     * </p>
     */
    public void runCommands() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        do {
            this.view.showCommandPrompt();
            String line = scanner.nextLine().trim();

            try {
                if (!line.isEmpty()) {
                    exit = this.runCommandLine(line);
                }
            } catch (Exception e) {
                this.view.showError("ERROR (" + e.getClass().getSimpleName() + ") >>> " + e.getMessage());
            }

        } while (!exit);
    }

    /**
     * Runs the CLI in file mode, reading commands line-by-line from the given file.
     * <p>
     * Each non-empty line is treated as a command line. The executed command is
     * echoed to the output to simulate the interactive prompt. The execution stops
     * if the {@link #EXIT} command is found or the file ends.
     * </p>
     *
     * @param fileName the path to the file containing commands
     * @throws IOException if the file cannot be opened or read
     */
    public void runCommandsFromFile(String fileName) throws IOException {
        try (Scanner fileScanner = new Scanner(Path.of(fileName))) {
            boolean exit = false;

            while (fileScanner.hasNextLine() && !exit) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                this.view.show("tUPM> " + line);

                try {
                    exit = this.runCommandLine(line);
                } catch (Exception e) {
                    this.view.showError("ERROR (" + e.getClass().getSimpleName() + ") >>> " + e.getMessage());
                }
            }
        }
    }

    /**
     * Resolves and executes a single command line.
     * <p>
     * The command is detected by matching the input against the registered command
     * names. Any remaining text after the command name is treated as parameters.
     * </p>
     *
     * @param line the raw input line
     * @return {@code true} if the command is {@link #EXIT} and execution should stop,
     *         {@code false} otherwise
     * @throws CommandException if the command does not exist
     */
    private boolean runCommandLine(String line) {
        String command = this.commands.keySet().stream()
                .filter(cmd -> line.equals(cmd) || line.startsWith(cmd + " "))
                .findFirst()
                .orElseThrow(() -> new CommandException("Command '" + line + "' does not exist."));

        String paramsPart = line.substring(command.length()).trim();
        Scanner paramScanner = new Scanner(paramsPart);
        String[] params = this.scanParamsIfNeededAssured(paramScanner, command);

        if (EXIT.equals(command)) {
            return true;
        } else {
            this.commands.get(command).execute(params);
            this.view.show("");
            return false;
        }
    }

    /**
     * Parses parameters for a given command if parameters are expected.
     * <p>
     * Parameters support quoted strings (e.g. {@code "My Product Name"}), and
     * whitespace is normalized. If no parameters are provided, an empty array
     * is returned.
     * </p>
     *
     * @param scanner the scanner positioned at the parameter portion of the input
     * @param command the resolved command name
     * @return an array of parsed parameters
     */
    private String[] scanParamsIfNeededAssured(Scanner scanner, String command) {
        List<String> expectedParams = commands.get(command).params();
        if (expectedParams.isEmpty()) {
            return new String[0];
        }

        String line;
        if (scanner.hasNextLine()) {
            line = scanner.nextLine().trim();
        } else if (scanner.hasNext()) {
            line = scanner.next().trim();
        } else {
            return new String[0];
        }

        if (line.isEmpty()) {
            return new String[0];
        }

        List<String> params = new ArrayList<>();
        Matcher m = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(line);

        while (m.find()) {
            String p = (m.group(1) != null) ? m.group(1) : m.group(2);

            p = p.trim();
            p = p.replaceAll("[\\t\\n\\r]", "");
            p = p.replaceAll("\\s{2,}", " ");

            if (!p.isEmpty()) {
                params.add(p);
            }
        }

        return params.toArray(new String[0]);
    }

    /**
     * Displays help information for all registered commands.
     * <p>
     * This method prints each command's help string and includes a brief summary
     * of available categories and discount rules for the shop domain.
     * </p>
     */
    public void help() {
        this.view.show("Commands:");
        for (Command command : this.commands.values()) {
            this.view.show("\t" + command.help());
        }
        this.view.show("");
        this.view.show("Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS");
        this.view.show("Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%, ELECTRONICS 3%.");
    }
}