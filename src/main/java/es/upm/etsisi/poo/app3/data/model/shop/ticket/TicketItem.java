package es.upm.etsisi.poo.app3.data.model.shop.ticket;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;

public abstract class TicketItem implements Comparable<TicketItem> {
    protected Purchasable<?> purchasable;
    protected Integer quantity;

    public TicketItem(Purchasable<?> purchasable, Integer quantity) {
        if (quantity <= 0) {
            throw new InvalidAttributeException("Quantity must be positive");
        }
        this.purchasable = purchasable;
        this.quantity = quantity;
    }

    public abstract Double getTotalPrice();

    public Purchasable<?> getPurchasable() {
        return this.purchasable;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return this.purchasable.toString();
    }

    @Override
    public int compareTo(TicketItem other) {
        boolean thisIsProduct = this.purchasable instanceof Product;
        boolean otherIsProduct = other.purchasable instanceof Product;

        if(thisIsProduct && !otherIsProduct) return -1;
        if(!thisIsProduct && otherIsProduct) return 1;

        if(thisIsProduct){
            String thisName = ((Product) this.purchasable).getName();
            String otherName = ((Product) other.purchasable).getName();
            return thisName.compareTo(otherName);
        }

        return this.purchasable.getId().toString()
                .compareTo(other.purchasable.getId().toString());
    }
}
