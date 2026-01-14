package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.Entity;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.TicketItem;
import jakarta.persistence.*;

@Inheritance(strategy = InheritanceType.JOINED) // Crea tablas específicas para hijos
@Table(name = "purchasables")
public abstract class Purchasable<T> extends Entity<T> {

    @Override
    @Id // Obligatorio para Hibernate
    public T getId() {
        return super.getId();
    }

    public abstract double getUnitPrice(TicketItem context);

    public abstract void validateAvailability();

}
