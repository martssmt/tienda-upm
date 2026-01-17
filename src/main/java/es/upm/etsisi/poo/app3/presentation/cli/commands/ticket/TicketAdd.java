package es.upm.etsisi.poo.app3.presentation.cli.commands.ticket;

import es.upm.etsisi.poo.app3.data.model.shop.products.CustomProduct;
import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;
import es.upm.etsisi.poo.app3.services.CashierService;
import es.upm.etsisi.poo.app3.services.PurchasableService;

import java.util.List;

/**
 * Command that adds a purchasable element to an existing ticket.
 * <p>
 * This command allows adding either a standard product/service or a
 * {@link CustomProduct} to a ticket managed by a cashier. In the case of
 * custom products, optional personalization texts can be provided.
 * </p>
 *
 * <p>
 * The operation is delegated to {@link CashierService}, while product lookup
 * is handled by {@link PurchasableService}.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Command
 * @see CashierService
 * @see PurchasableService
 * @see Ticket
 * @see Purchasable
 * @see CustomProduct
 */
public class TicketAdd implements Command {

    /**
     * Service used to manage cashier and ticket operations.
     */
    private final CashierService cashierService;

    /**
     * Service used to retrieve purchasable elements.
     */
    private final PurchasableService purchasableService;

    /**
     * View used to display feedback and results.
     */
    private final View view;

    /**
     * Creates a new {@code ticket add} command.
     *
     * @param view               the view used to display output
     * @param cashierService     the service used to manage tickets
     * @param purchasableService the service used to retrieve purchasables
     */
    public TicketAdd(View view, CashierService cashierService, PurchasableService purchasableService) {
        this.cashierService = cashierService;
        this.purchasableService = purchasableService;
        this.view = view;
    }

    /**
     * Returns the command name.
     *
     * @return the string {@code "ticket add"}
     */
    @Override
    public String name() {
        return "ticket add";
    }

    /**
     * Returns the list of expected parameters.
     *
     * @return parameter descriptors for the command
     */
    @Override
    public List<String> params() {
        return List.of("<ticketId>", "<cashId>", "<prodId>", "<amount>", "[--p<txt> --p<txt>]");
    }

    /**
     * Returns the help message for this command.
     *
     * @return the help message
     */
    @Override
    public String helpMessage() {
        return "Implements a new ticket with ticketId, cashId, productId, amount and optional personalizations.";
    }

    /**
     * Validates the provided parameters.
     * <p>
     * This command requires at least the ticket identifier, cashier identifier,
     * and product identifier.
     * </p>
     *
     * @param params raw parameters provided by the user
     * @return the validated parameters
     * @throws CommandException if the parameters are insufficient
     */
    @Override
    public String[] assessParams(String[] params) {
        if (params.length < 3) {
            throw new CommandException("Usage: " + this.help());
        }
        return params;
    }

    /**
     * Executes the command by adding a purchasable element to a ticket.
     * <p>
     * If the purchasable is a {@link CustomProduct}, personalization texts
     * are extracted from the remaining parameters and applied accordingly.
     * </p>
     *
     * @param params the command parameters
     */
    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);

        String ticketid = params[0];
        String cashid = params[1];
        String prodid = params[2];

        Integer amount = null;
        if (params.length >= 4) {
            amount = Integer.parseInt(params[3]);
        }

        Purchasable purchasable = this.purchasableService.findProd(prodid);
        Ticket ticket;

        if (purchasable instanceof CustomProduct) {
            String[] texts = java.util.Arrays.copyOfRange(params, 4, params.length);
            ticket = this.cashierService.addCustomProduct(
                    cashid, ticketid, (CustomProduct) purchasable, amount, texts
            );
        } else {
            ticket = this.cashierService.addProduct(cashid, ticketid, purchasable, amount);
        }

        this.view.showEntity(ticket);
        this.view.show("ticket add: ok");
    }
}