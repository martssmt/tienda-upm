package es.upm.etsisi.poo.Model;

import es.upm.etsisi.poo.Model.Product;

public class TicketItem {
    private Product product;
    private int quantity;
    private double discountApplied;

    public TicketItem(Product product, int quantity, double discountApplied) {
        this.product = product;
        this.quantity = quantity;
        this.discountApplied = discountApplied;
    }

}
