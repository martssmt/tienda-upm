package es.upm.etsisi.poo.app3.data.model.shop.ticket;

import es.upm.etsisi.poo.app3.data.model.shop.TimeProductType;
import es.upm.etsisi.poo.app3.data.model.shop.products.TimeProduct;

public class TimeTicketItem extends TicketItem {
    public TimeTicketItem(TimeProduct timeProduct, Integer quantity) {
        super(timeProduct, quantity);
    }

    private TimeProduct getProduct(){
        return (TimeProduct) this.purchasable;
    }

    @Override
    public Double getTotalPrice() {
        return getProduct().getPrice() * this.quantity;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        TimeProduct product = this.getProduct();
        String productType;
        if (product.getType() == TimeProductType.MEETING) {
            productType = "Meeting";
        } else {
            productType = "Food";
        }

        stringBuilder.append("{class:").append(productType)
                .append(", id:").append(product.getId())
                .append(", name:'").append(product.getName()).append("'")
                .append(", price:").append(product.getPrice())
                .append(", date of Event:");

        if (product.getOpenDate() == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(product.getOpenDate());
        }

        stringBuilder.append(", max people allowed:");
        if (product.getMaxPeople() == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(product.getMaxPeople());
        }

        stringBuilder.append(", actual people in event:");
        if (this.quantity == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.quantity);
        }

        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}
