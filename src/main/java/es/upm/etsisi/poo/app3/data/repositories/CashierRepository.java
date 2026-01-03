package es.upm.etsisi.poo.app3.data.repositories;

import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.data.model.user.Cashier;

import java.util.List;

public interface CashierRepository extends RepositoryUser<Cashier> {

    List<Ticket> listTickets();

    void add(Cashier cashier);

}
