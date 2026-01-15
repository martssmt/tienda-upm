package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.shop.Category;
import jakarta.persistence.*;

@jakarta.persistence.Entity
@Table(name = "basic_products")
public class BasicProduct extends Product {

    @Enumerated(EnumType.STRING)
    private Category category;

    protected BasicProduct() {
        super();
    }

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
        return "{class:Product, id:" + this.getIdAsInt() + ", name:'" + this.getName() +
                "', category:" + this.category + ", price:" + this.getPrice() + "}";
    }

    @Override
    public void validateAvailability() {}

}
