package es.upm.etsisi.poo.Controller;

import es.upm.etsisi.poo.Model.Catalog;
import es.upm.etsisi.poo.Model.Category;
import es.upm.etsisi.poo.Model.Product;
import es.upm.etsisi.poo.View.ConsoleView;

public class ProductController {

    private Catalog catalog;

    public ProductController() {
        this.catalog = new Catalog();
    }

    public Catalog getCatalog() {
        return this.catalog;
    }

    public void handleAdd(int id, String name, Category category, double price) {
        Product product = new Product(id, name, category, price);
        if (this.catalog.addProduct(product)) {
            ConsoleView.showMessage("Product with id " + id + " already exists in the catalog.");
            ConsoleView.showMessage("prod add: error");
        } else {
            ConsoleView.showProduct(product);
            ConsoleView.showMessage("prod add: ok");
        }
    }

    public void handleList() {
        ConsoleView.showCatalog(this.catalog.toString());
        ConsoleView.showMessage("prod list: ok");
    }

    public void handleUpdate(int id, String field, String value) {
        if (this.catalog.updateProduct(id, field, value)) {
            ConsoleView.showMessage("prod update: ok");
        } else {
            ConsoleView.showMessage("prod update: error");
        }
    }

    public void handleRemove(int id) {
        if (this.catalog.removeProduct(id)) {
            ConsoleView.showMessage("prod remove: ok");
        } else {
            ConsoleView.showMessage("Product with id " + id + " already does not exist in the catalog.");
            ConsoleView.showMessage("prod remove: error");
        }
    }

}
