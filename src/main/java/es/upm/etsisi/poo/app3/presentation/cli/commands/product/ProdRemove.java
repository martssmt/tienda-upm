package es.upm.etsisi.poo.app3.presentation.cli.commands.product;

import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;
import es.upm.etsisi.poo.app3.services.PurchasableService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

public class ProdRemove implements Command {

    private final PurchasableService purchasableService;
    private final View view;

    public ProdRemove(View view, PurchasableService purchasableService) {
        this.purchasableService = purchasableService;
        this.view = view;
    }

    @Override
    public String name() {
        return "prod remove";
    }

    @Override
    public List<String> params() {
        return List.of("<id>");
    }

    @Override
    public String helpMessage() {
        return "Deletes an existing product by its id.";
    }

    @Override
    public String[] assessParams(String[] params) {
        if (params.length != 1 || !params[0].matches("-?\\d+")) {
            throw new CommandException("Usage: " + this.help());
        }
        return params;
    }

    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);
        String id = params[0];
        Purchasable purchasable = this.purchasableService.remove(id);
        this.view.showEntity(purchasable);
        this.view.show("prod remove: ok");
    }
}