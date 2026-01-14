package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.Entity;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.TicketItem;

public abstract class Purchasable<T> extends Entity<T> {

    public abstract double getUnitPrice(TicketItem context);

    public abstract void validateAvailability();

}
