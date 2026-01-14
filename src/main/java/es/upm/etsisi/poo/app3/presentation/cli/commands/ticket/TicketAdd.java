package es.upm.etsisi.poo.app3.presentation.cli.commands.ticket;

import es.upm.etsisi.poo.app3.data.model.shop.products.BasicProduct;
import es.upm.etsisi.poo.app3.data.model.shop.products.CustomProduct;
import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.services.ProductService;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.services.CashierService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

public class TicketAdd implements Command {

    private final CashierService cashierService;
    private final ProductService productService;
    private final View view;

    public TicketAdd(View view, CashierService cashierService, ProductService productService) {
        this.cashierService = cashierService;
        this.productService = productService;
        this.view = view;
    }

    @Override
    public String name() {
        return "ticket add";
    }

    @Override
    public List<String> params() {
        return List.of("<ticketId>", "<cashId>", "<prodId>", "<amount>", "[--p<txt> --p<txt>]");
    }

    @Override
    public String helpMessage() {
        return "Implements a new ticket with ticketId, cashId, productId, amount and optional personalizations.";
    }

    @Override
    public String[] assessParams(String[] params) {
        if (params.length < 4 || !params[2].matches("-?\\d+") || !params[3].matches("-?\\d+"))
            throw new CommandException("Usage: " + this.help());
        return params;
    }

    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);
        String ticketid = params[0];
        String cashid = params[1];
        Integer prodid = Integer.parseInt(params[2]);
        Integer amount = Integer.parseInt(params[3]);
        Product product = this.productService.findProd(prodid);
        Ticket ticket;
        if (product instanceof CustomProduct) {
            String[] texts = java.util.Arrays.copyOfRange(params, 4, params.length);
            ticket = this.cashierService.addCustomProduct(cashid, ticketid, (CustomProduct) product, amount, texts);
        } else if (product instanceof BasicProduct) {
            ticket = this.cashierService.addProduct(cashid, ticketid, product, amount);
        } else {
            ticket = this.cashierService.addService(cashid, ticketid, product);
        }
        this.view.showEntity(ticket);
        this.view.show("ticket add: ok");
    }
}