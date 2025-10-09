package es.upm.etsisi.poo.controller;

import es.upm.etsisi.poo.model.Catalog;
import es.upm.etsisi.poo.model.Category;
import es.upm.etsisi.poo.model.Product;
import es.upm.etsisi.poo.view.ConsoleView;

/**
 * The {@code ProductController} class is responsible for handling all operations
 * related to {@link Product} objects within the application.
 * <p>
 * It serves as the intermediary between the {@link CommandController} (user commands)
 * and the {@link Catalog} (data model). This controller manages product creation,
 * updates, removal, and listing, ensuring proper interaction between the model and view layers.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *     <li>Add new products to the catalog.</li>
 *     <li>List all available products.</li>
 *     <li>Update existing products (name, category, or price).</li>
 *     <li>Remove products from the catalog.</li>
 * </ul>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * ProductController controller = new ProductController();
 * controller.handleAdd(1, "Notebook", Category.STATIONERY, 5.99);
 * controller.handleList();
 * controller.handleUpdate(1, "price", "6.49");
 * controller.handleRemove(1);
 * }</pre>
 *
 * @author Marta
 * @version 1.0
 * @see Catalog
 * @see Product
 * @see Category
 * @see ConsoleView
 */

public class ProductController {

    /** The catalog containing all registered products. */
    private final Catalog catalog;

    /**
     * Constructs a {@code ProductController} and initializes a new {@link Catalog}.
     */
    public ProductController() {
        this.catalog = new Catalog();
    }

    /**
     * Returns the catalog managed by this controller.
     *
     * @return the current {@link Catalog} instance
     */
    public Catalog getCatalog() {
        return this.catalog;
    }

    /**
     * Handles the addition of a new product to the catalog.
     * <p>
     * This method creates a {@link Product} instance using the provided parameters,
     * adds it to the catalog, and displays the result on the console.
     * </p>
     *
     * @param id        the unique identifier of the product
     * @param name      the name of the product
     * @param category  the category to which the product belongs
     * @param price     the price of the product
     * @throws IllegalArgumentException if any argument is invalid
     * @see Product
     * @see Catalog#addProduct(Product)
     */
    public void handleAdd(int id, String name, Category category, double price) {
        Product product = new Product(id, name, category, price);
        this.catalog.addProduct(product);
        ConsoleView.showProduct(product);
        ConsoleView.showMessage("prod add: ok");
    }

    /**
     * Displays all products currently stored in the catalog.
     */
    public void handleList() {
        ConsoleView.showCatalog(this.catalog);
        ConsoleView.showMessage("prod list: ok");
    }

    /**
     * Handles the update of an existing product in the catalog.
     * <p>
     * This method delegates the update to {@link Catalog#updateProduct(int, String, String)},
     * then displays the updated product information and confirmation message.
     * </p>
     *
     * @param id     the ID of the product to update
     * @param field  the field to modify (e.g., "NAME", "CATEGORY", "PRICE")
     * @param value  the new value to assign to the field
     * @throws IllegalArgumentException if the field or value is invalid
     * @see Catalog#updateProduct(int, String, String)
     */
    public void handleUpdate(int id, String field, String value) {
        this.catalog.updateProduct(id, field, value);
        ConsoleView.showProduct(this.catalog.getProduct(id));
        ConsoleView.showMessage("prod update: ok");
    }

    /**
     * Handles the removal of a product from the catalog by its ID.
     * <p>
     * The method first displays the product being removed, then calls
     * {@link Catalog#removeProduct(int)} to perform the deletion.
     * </p>
     *
     * @param id the ID of the product to remove
     * @throws IllegalArgumentException if the product ID is invalid or not found
     * @see Catalog#removeProduct(int)
     */
    public void handleRemove(int id) {
        ConsoleView.showProduct(this.catalog.getProduct(id));
        this.catalog.removeProduct(id);
        ConsoleView.showMessage("prod remove: ok");
    }
}