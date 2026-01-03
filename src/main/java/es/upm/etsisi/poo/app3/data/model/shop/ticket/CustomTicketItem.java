package es.upm.etsisi.poo.app3.data.model.shop.ticket;

import es.upm.etsisi.poo.app3.data.model.shop.products.BasicProduct;
import es.upm.etsisi.poo.app3.data.model.shop.products.CustomProduct;

public class CustomTicketItem extends BasicTicketItem {
    private final String[] texts;

    public CustomTicketItem(CustomProduct customProduct, Integer quantity, Double discountApplied, String[] texts) {
        super(customProduct, quantity, discountApplied);
        this.texts = texts;
        for (int i = 0; i < texts.length; i++) {
            texts[i] = texts[i].substring(3);
        }
    }

    @Override
    public Double getTotalPrice() {
        CustomProduct customProduct = (CustomProduct) this.getProduct();
        double originalPrice = customProduct.getPrice();
        double finalPrice = originalPrice * (1 + 0.1 * this.texts.length);
        return finalPrice * this.quantity;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{class:ProductPersonalized, ");
        stringBuilder.append("id:").append(this.product.getId()).append(", ");
        stringBuilder.append("name:'").append(this.product.getName()).append("', ");
        stringBuilder.append("category:").append(((BasicProduct) this.product).getCategory()).append(", ");

        double perUnitPrice = this.getTotalPrice() / this.quantity;
        stringBuilder.append("price:").append(perUnitPrice).append(", ");

        stringBuilder.append("maxPersonal:").append(((CustomProduct) this.product).getNumberTexts());

        if (this.texts.length != 0) {
            stringBuilder.append(", personalizationList:[");
            for (int i = 0; i < this.texts.length; i++) {
                stringBuilder.append(this.texts[i]);
                if (i < this.texts.length - 1) {
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append("]");
        }
        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}
