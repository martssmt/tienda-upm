package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;

import java.util.Objects;

public abstract class Product extends Purchasable<Integer> {
    private String name;
    private Double price;

    public Product(String name, Double price) {
        super();
        this.name = name;
        this.price = price;
    }

    public Product(Product original) {
        super();
        this.name = original.name;
        this.price = original.price;
    }

    public boolean equalsWithoutId(Product product) {
        if (product == null || !Objects.equals(this.getClass(), product.getClass())) {
            return false;
        }
        return Objects.equals(this.name, product.name)
                && Objects.equals(this.price, product.getPrice());
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        if (price < 0) {
            throw new InvalidAttributeException("Price cannot be negative");
        }
        this.price = price;
    }

    @Override
    public String toString() {
        return "{class:Product, id:" + this.getId() + ", name:'" + this.getName() +
                "', price:" + this.getPrice() + "}";
    }

    public void setId(Integer id) {
        if (id < 0) {
            throw new InvalidAttributeException("Id cannot be negative");
        }
        this.id = id;
    }
}
