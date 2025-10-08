package es.upm.etsisi.poo.model;

public enum Category {
    MERCH(0),
    STATIONERY(0.05),
    CLOTHES(0.07),
    BOOK(0.1),
    ELECTRONICS(0.03);

    private final double discount;

    Category(double discount) {
        this.discount=discount;
    }

    public double getDiscount(){
        return this.discount;
    }
}
