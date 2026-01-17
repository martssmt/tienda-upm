package es.upm.etsisi.poo.app3.presentation.cli.commands.product;

import es.upm.etsisi.poo.app3.data.model.shop.TimeProductType;
import es.upm.etsisi.poo.app3.data.model.shop.products.TimeProduct;
import es.upm.etsisi.poo.app3.services.PurchasableService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.time.LocalDate;
import java.util.List;

/**
 * Command that adds a new {@link TimeProduct} of type {@link TimeProductType#FOOD}.
 * <p>
 * This command creates a time-dependent food product with an event/open date and
 * a maximum number of people. An optional numeric identifier may be provided.
 * The created product is persisted through {@link PurchasableService}.
 * </p>
 *
 * <p>
 * Expected parameters:
 * <ul>
 *   <li>{@code [<id>]} (optional)</li>
 *   <li>{@code "<name>"}</li>
 *   <li>{@code <price>}</li>
 *   <li>{@code <expiration: yyyy-MM-dd>} (event/open date)</li>
 *   <li>{@code <max_people>}</li>
 * </ul>
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see PurchasableService
 * @see TimeProduct
 * @see TimeProductType
 */
public class ProdAddFood implements Command {

    /**
     * Service used to manage purchasable persistence and business rules.
     */
    private final PurchasableService purchasableService;

    /**
     * View used to display feedback and results.
     */
    private final View view;

    /**
     * Creates a new {@code prod addFood} command.
     *
     * @param view              the view used to display output
     * @param purchasableService the service used to add purchasables
     */
    public ProdAddFood(View view, PurchasableService purchasableService) {
        this.purchasableService = purchasableService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "prod addFood"}
     */
    @Override
    public String name() {
        return "prod addFood";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return parameter descriptors for the command
     */
    @Override
    public List<String> params() {
        return List.of("[<id>]", "\"<name>\"", "<price>", "<expiration:yyyy-MM-dd>", "<max_people>");
    }

    /**
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Implements a new food product with optional id, name, price, expiration date and max people.";
    }

    /**
     * Validates and normalizes the provided parameters.
     * <p>
     * The returned array is normalized as:
     * {@code [id, name, price, expiration, max_people]}.
     * </p>
     *
     * @param params raw parameters provided by the user
     * @return normalized parameters
     * @throws CommandException if the parameters do not match the expected format
     */
    @Override
    public String[] assessParams(String[] params) {
        if (params == null || params.length < 4 || params.length > 5) {
            throw new CommandException("Usage: " + this.help());
        }

        int index = 0;

        // Optional id
        String id = null;
        if (params[0].matches("-?\\d+")) {
            id = params[0];
            index++;
        }

        // Name
        String name = params[index];
        if (name.isEmpty()) {
            throw new CommandException("Usage: " + this.help());
        }
        index++;

        // Price
        if (!params[index].matches("[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?")) {
            throw new CommandException("Usage: " + this.help());
        }
        String price = params[index];
        index++;

        // Expiration/Event date
        if (!params[index].matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new CommandException("Usage: " + this.help());
        }
        String expiration = params[index];
        index++;

        // Max people
        if (!params[index].matches("-?\\d+")) {
            throw new CommandException("Usage: " + this.help());
        }
        String max_people = params[index];

        return new String[]{id, name.trim(), price, expiration, max_people};
    }

    /**
     * Executes the command by creating and persisting a food time product.
     *
     * @param params raw parameters provided by the user
     */
    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);

        String id = params[0];
        String name = params[1];
        Double price = Double.valueOf(params[2]);
        LocalDate expiration = LocalDate.parse(params[3]);
        Integer maxPeople = Integer.valueOf(params[4]);

        TimeProduct product = new TimeProduct(name, TimeProductType.FOOD, price, expiration, maxPeople);

        if (id != null) {
            this.purchasableService.add(product, id);
        } else {
            this.purchasableService.add(product);
        }

        this.view.showEntity(product);
        this.view.show("prod addFood: ok");
    }
}