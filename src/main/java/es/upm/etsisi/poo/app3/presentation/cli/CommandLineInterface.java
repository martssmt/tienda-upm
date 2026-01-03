package es.upm.etsisi.poo.app3.presentation.cli;

import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLineInterface {
    public static final String EXIT = "exit";
    private final Map<String, Command> commands;
    private final View view;

    public CommandLineInterface(View view) {
        this.view = view;
        this.commands = new LinkedHashMap<>();
    }

    public void add(Command command) {
        this.commands.put(command.name(), command);
    }

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
            String p;
            if (m.group(1) != null) {
                p = m.group(1);
            } else {
                p = m.group(2);
            }
            p = p.trim();
            p = p.replaceAll("[\\t\\n\\r]", "");
            p = p.replaceAll("\\s{2,}", " ");

            if (!p.isEmpty()) {
                params.add(p);
            }
        }
        return params.toArray(new String[0]);
    }

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