package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.Category;

public class CustomProduct extends BasicProduct {
    private Double originalPrice;
    private Integer numberTexts;

    public CustomProduct(String name, Category category, Double originalPrice, Integer numberTexts) {
        super(name, category, originalPrice);
        this.originalPrice = originalPrice;
        this.numberTexts = numberTexts;
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
    public String toString() {
        return "{class:ProductPersonalized, id:" + this.id + ", name:'" + this.getName() + "', category:" +
                this.getCategory() + ", price:" + this.originalPrice + ", maxPersonal:" + this.numberTexts + "}";
    }
}
