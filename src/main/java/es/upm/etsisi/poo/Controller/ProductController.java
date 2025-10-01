package es.upm.etsisi.poo.Controller;

import es.upm.etsisi.poo.Model.Catalog;
import es.upm.etsisi.poo.Model.Category;
import es.upm.etsisi.poo.Model.Product;
import es.upm.etsisi.poo.View.ConsoleView;

public class ProductController {

    private Catalog catalog;
    private ConsoleView view;

    public ProductController(ConsoleView view) {
        this.catalog = new Catalog();
        this.view = view;
    }

    public Catalog getCatalog() {
        return this.catalog;
    }

    public void handleAdd(int id, String name, Category category, double price) {
        Product product = new Product(id, name, category, price);
        if (this.catalog.addProduct(product)) {
            this.view.showMessage("Product with id " + id + " already exists in the catalog.");
            this.view.showMessage("prod add: error");
        } else {
            this.view.showProduct(product);
            this.view.showMessage("prod add: ok");
        }
    }

    public void handleList() {
        this.view.showCatalog(this.catalog.toString());
        this.view.showMessage("prod list: ok");
    }

    public void handleUpdate(int id, String field, String value) {
        if (field.equals("NAME") || field.equals("CATEGORY") || field.equals("PRICE")) {
            if (this.catalog.updateProduct(id, field, value)) {
                this.view.showMessage("prod update: ok");
            } else {
                this.view.showMessage("prod update: error");
            }
        } else {
            this.view.showMessage("Unknown field: " + field);
            this.view.showMessage("prod update: error");
        }
    }

    public void handleRemove(int id) {
        if (this.catalog.removeProduct(id)) {
            this.view.showMessage("prod remove: ok");
        } else {
            this.view.showMessage("Product with id " + id + " already does not exist in the catalog.");
            this.view.showMessage("prod remove: error");
        }
    }

}
