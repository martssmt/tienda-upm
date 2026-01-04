package es.upm.etsisi.poo.app3.presentation.cli.commands.product;

import es.upm.etsisi.poo.app3.data.model.shop.Category;
import es.upm.etsisi.poo.app3.data.model.shop.products.BasicProduct;
import es.upm.etsisi.poo.app3.data.model.shop.products.CustomProduct;
import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.services.ProductService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.util.List;

public class ProdAdd implements Command {

    private final ProductService productService;
    private final View view;

    public ProdAdd(View view, ProductService productService) {
        this.productService = productService;
        this.view = view;
    }

    @Override
    public String name() {
        return "prod add";
    }

    @Override
    public List<String> params() {
        return List.of("[<id>]", "\"<name>\"", "<category>", "<price>", "[<maxPers>]");
    }

    @Override
    public String helpMessage() {
        return "Adds a new product to the catalog with optional id, name, category, price (depending on the product type) and optional max people.";
    }

    @Override
    public String[] assessParams(String[] params) {
        if (params == null || params.length < 2 || params.length > 5)
            throw new CommandException("Usage: " + this.help());
        int index = 0;
        // Id
        String id = null;
        if (params[0].matches("-?\\d+") && params.length >= 4) {
            id = params[0];
            index++;

        } else {
            if (params.length > 4)
                throw new CommandException("Usage: " + this.help());
        }
        // Name
        String name = params[index];
        if (name.isEmpty())
            throw new CommandException("Usage: " + this.help());
        index++;
        // Category
        String category = params[index];
        if (!category.equals("MERCH") && !category.equals("STATIONARY")
                && !category.equals("CLOTHES") && !category.equals("BOOK")
                && !category.equals("ELECTRONICS"))
            throw new CommandException("Usage: " + this.help());
        index++;
        // Price
        String price = "0";
        if (index < params.length) {
            price = params[index];
            if (!price.matches("[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?")) {
                throw new CommandException("Usage: " + this.help());
            }
        }
        index++;
        // maxPeople (optional)
        String maxPeople = null;
        if (index < params.length) {
            maxPeople = params[index];
            if (!maxPeople.matches("-?\\d+"))
                throw new CommandException("Usage: " + this.help());
        }
        // Return
        return new String[]{id, name.trim(), category, price, maxPeople};
    }

    @Override
    public void execute(String[] params) {
        params = this.assessParams(params);
        String id = params[0];
        String name = params[1];
        Category category = Category.valueOf(params[2]);
        Double price = Double.parseDouble(params[3]);
        Integer numberTexts = null;
        if (params[4] != null) {
            numberTexts = Integer.parseInt(params[4]);
        }
        Product product;
        if (numberTexts == null) {
            product = new BasicProduct(name, category, price);
        } else {
            product = new CustomProduct(name, category, price, numberTexts);
        }
        if (id == null) {
            this.productService.add(product);
        } else {
            this.productService.add(product, id);
        }
        this.view.showEntity(product);
        this.view.show("prod add: ok");
    }
}