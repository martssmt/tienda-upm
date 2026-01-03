package es.upm.etsisi.poo.app3.presentation.cli.commands.product;

import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.services.ProductService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

public class ProdList implements Command {

    private final ProductService productService;
    private final View view;

    public ProdList(View view, ProductService productService) {
        this.productService = productService;
        this.view = view;
    }

    @Override
    public String name() {
        return "prod list";
    }

    @Override
    public List<String> params() {
        return List.of();
    }

    @Override
    public String helpMessage() {
        return "Lists all registered products with their id, name, price and stock.";
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
        List<Product> products = this.productService.list();
        this.view.showList("Catalog:", products);
        this.view.show("prod list: ok");
    }
}