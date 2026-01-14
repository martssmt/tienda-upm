package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.TicketItem;
import jakarta.persistence.*;

import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Product extends Purchasable<Integer> {

    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private Double price;

    protected Product() {
        super();
    }

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

    @Override
    public double getUnitPrice(TicketItem context) {
        return this.price;
    }
}
