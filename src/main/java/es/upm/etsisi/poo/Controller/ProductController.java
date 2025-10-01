package es.upm.etsisi.poo.Controller;

import es.upm.etsisi.poo.Model.Catalog;
import es.upm.etsisi.poo.View.ConsoleView;

public class ProductController {

    private Catalog catalog;
    private ConsoleView view;

    public ProductController(ConsoleView view) {
        this.catalog = new Catalog();
        this.view = view;
    }

    public Catalog getCatalog() {
        return catalog;
    }


}
