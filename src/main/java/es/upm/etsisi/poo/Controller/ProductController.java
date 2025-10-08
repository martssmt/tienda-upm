package es.upm.etsisi.poo.Controller;

import es.upm.etsisi.poo.Model.Catalog;
import es.upm.etsisi.poo.Model.Category;
import es.upm.etsisi.poo.Model.Product;
import es.upm.etsisi.poo.View.ConsoleView;

public class ProductController {

    private final Catalog catalog;

    public ProductController() {
        this.catalog = new Catalog();
    }

    public Catalog getCatalog() {
        return this.catalog;
    }

    public void handleAdd(int id, String name, Category category, double price) {
        Product product = new Product(id, name, category, price);
        this.catalog.addProduct(product);
        ConsoleView.showProduct(product);
        ConsoleView.showMessage("prod add: ok");
    }

    public void handleList() {
        ConsoleView.showCatalog(this.catalog);
        ConsoleView.showMessage("prod list: ok");
    }

    public void handleUpdate(int id, String field, String value) {
        this.catalog.updateProduct(id, field, value);
        ConsoleView.showMessage("prod update: ok");
    }

    public void handleRemove(int id) {
        this.catalog.removeProduct(id);
        ConsoleView.showMessage("prod remove: ok");
    }

}
