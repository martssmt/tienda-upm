package es.upm.etsisi.poo.model;

public class TicketItem implements Comparable<TicketItem> {
    private final Product product;
    private int quantity;
    private final double discountApplied;

    public TicketItem(Product product, int quantity, double discountApplied) {
        this.product = product;
        this.quantity = quantity;
        this.discountApplied = discountApplied;
    }

    public double getSubtotal(){
        return this.product.getPrice() * this.quantity;
    }

    public double getDiscount(){
        return this.discountApplied * this.getSubtotal();
    }

    public Product getProduct() {
        return this.product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int compareTo(TicketItem ticketItem) {
        return this.product.getName().compareTo(ticketItem.getProduct().getName());
    }
}
