package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.shop.Category;

public class BasicProduct extends Product {

    private Category category;

    public BasicProduct(String name, Category category, Double price) {
        super(name, price);
        this.category = category;
    }

    public BasicProduct(BasicProduct original) {
        super(original);
        this.category = original.getCategory();
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "{class:Product, id:" + this.getId() + ", name:'" + this.getName() +
                "', category:" + this.category + ", price:" + this.getPrice() + "}";
    }
}
