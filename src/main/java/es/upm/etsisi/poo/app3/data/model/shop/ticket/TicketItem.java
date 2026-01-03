package es.upm.etsisi.poo.app3.data.model.shop.ticket;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.products.Product;

public abstract class TicketItem implements Comparable<TicketItem> {
    protected Product product;
    protected Integer quantity;

    public TicketItem(Product product, Integer quantity) {
        if (quantity < 0) {
            throw new InvalidAttributeException("Quantity must be positive");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public abstract Double getTotalPrice();

    public Product getProduct() {
        return this.product;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return this.product.toString();
    }

    @Override
    public int compareTo(TicketItem ticketItem) {
        String nameProduct = ticketItem.getProduct().getName();
        return this.product.getName().compareTo(nameProduct);
    }
}
