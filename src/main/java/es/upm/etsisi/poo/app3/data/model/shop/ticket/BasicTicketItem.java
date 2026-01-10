package es.upm.etsisi.poo.app3.data.model.shop.ticket;

import es.upm.etsisi.poo.app3.data.model.shop.products.BasicProduct;

public class BasicTicketItem extends TicketItem {
    protected Double discountApplied;

    public BasicTicketItem(BasicProduct basicProduct, Integer quantity, Double discountApplied) {
        super(basicProduct, quantity);
        this.discountApplied = discountApplied;
    }

    protected BasicProduct getProduct() {
        return (BasicProduct) this.purchasable;
    }

    public Double getDiscountApplied() {
        return this.discountApplied;
    }

    public void setDiscountApplied(Double discountApplied) {
        this.discountApplied = discountApplied;
    }

    @Override
    public Double getTotalPrice() {
        return getProduct().getPrice() * quantity;
    }

    public Double getDiscount() {
        return this.discountApplied * this.getTotalPrice();
    }

}
