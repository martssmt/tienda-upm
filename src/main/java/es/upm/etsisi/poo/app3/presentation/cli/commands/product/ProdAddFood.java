package es.upm.etsisi.poo.app3.presentation.cli.commands.product;

import es.upm.etsisi.poo.app3.data.model.shop.TimeProductType;
import es.upm.etsisi.poo.app3.data.model.shop.products.TimeProduct;
import es.upm.etsisi.poo.app3.services.ProductService;
import es.upm.etsisi.poo.app3.presentation.cli.Command;
import es.upm.etsisi.poo.app3.presentation.cli.exceptions.CommandException;
import es.upm.etsisi.poo.app3.presentation.view.View;

import java.time.LocalDate;
import java.util.List;

public class ProdAddFood implements Command {

    private final ProductService productService;
    private final View view;

    public ProdAddFood(View view, ProductService productService) {
        this.productService = productService;
        this.view = view;
    }

    @Override
    public String name() {
        return "prod addFood";
    }

    @Override
    public List<String> params() {
        return List.of("[<id>]", "\"<name>\"", "<price>", "<expiration:yyyy-MM-dd>", "<max_people>");
    }

    @Override
    public String helpMessage() {
        return "Implements a new food product with optional id, name, price, expiration date and max people.";
    }

    @Override
    public String[] assessParams(String[] params) {
        if (params == null || params.length < 4 || params.length > 5)
            throw new CommandException("Usage: " + this.help());
        int index = 0;
        // Id
        String id = null;
        if (params[0].matches("-?\\d+")) {
            id = params[0];
            index++;
        }
        // Name
        String name = params[index];
        if (name.isEmpty())
            throw new CommandException("Usage: " + this.help());
        index++;
        // Price
        if (!params[index].matches("[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?"))
            throw new CommandException("Usage: " + this.help());
        String price = params[index];
        index++;
        // Expiration Date
        if (!params[index].matches("\\d{4}-\\d{2}-\\d{2}"))
            throw new CommandException("Usage: " + this.help());
        String expiration = params[index];
        index++;
        // Max_people
        if (!params[index].matches("-?\\d+"))
            throw new CommandException("Usage: " + this.help());
        String max_people = params[index];
        // Return
        return new String[]{id, name.trim(), price, expiration, max_people};
    }

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
            this.productService.add(product, id);
        } else {
            this.productService.add(product);
        }
        this.view.showEntity(product);
        this.view.show("prod addFood: ok");
    }
}