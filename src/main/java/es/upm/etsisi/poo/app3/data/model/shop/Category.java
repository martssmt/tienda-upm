package es.upm.etsisi.poo.app3.data.model.shop;

public enum Category {
    MERCH(0.0),
    STATIONERY(0.05),
    CLOTHES(0.07),
    BOOK(0.10),
    ELECTRONICS(0.03);

    private final double discount;

    Category(double discount) {
        this.discount = discount;
    }

    public double getDiscount() {
        return this.discount;
    }
}
