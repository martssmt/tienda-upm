package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.Category;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.TicketItem;
import jakarta.persistence.*;

@jakarta.persistence.Entity
@Table(name = "custom_products")
public class CustomProduct extends BasicProduct {

    @Column(name = "original_price")
    private Double originalPrice;
    @Column(name = "number_texts")
    private Integer numberTexts;

    protected CustomProduct() {
        super();
    }

    public CustomProduct(String name, Category category, Double originalPrice, Integer numberTexts) {
        super(name, category, originalPrice);
        this.originalPrice = originalPrice;
        this.numberTexts = numberTexts;
    }

    public CustomProduct(CustomProduct original) {
        super(original);
        this.originalPrice = original.originalPrice;
        this.numberTexts = original.numberTexts;
    }

    public Double getOriginalPrice() {
        return this.originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        if (originalPrice < 0) {
            throw new InvalidAttributeException("OriginalPrice cannot be negative");
        }
        this.originalPrice = originalPrice;
    }

    public Integer getNumberTexts() {
        return numberTexts;
    }

    public void setNumberTexts(Integer numberTexts) {
        if (numberTexts < 0) {
            throw new InvalidAttributeException("NumberTexts cannot be negative");
        }
        this.numberTexts = numberTexts;
    }

    @Override
    public double getUnitPrice(TicketItem context) {
        return this.originalPrice * (1 + 0.1 * context.getCustomTextsSize());
    }

    @Override
    public String toString() {
        return "{class:ProductPersonalized, id:" + this.id + ", name:'" + this.getName() + "', category:" +
                this.getCategory() + ", price:" + this.originalPrice + ", maxPersonal:" + this.numberTexts + "}";
    }
}
