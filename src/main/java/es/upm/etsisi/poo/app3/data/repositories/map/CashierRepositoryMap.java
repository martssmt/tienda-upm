package es.upm.etsisi.poo.app3.data.repositories.map;

import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.data.model.user.Cashier;
import es.upm.etsisi.poo.app3.data.repositories.CashierRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CashierRepositoryMap extends RepositoryUserMap<Cashier> implements CashierRepository {

    public CashierRepositoryMap() {
        super();
    }

    @Override
    public void add(Cashier cashier) {
        Random random = new Random();

        do {
            int number = random.nextInt(10000000);
            this.id = String.format("UW%07d", number);
        } while (this.map.containsKey(this.id)); // Repite si ya existe

        cashier.setId(this.id);
        this.map.put(this.id, cashier);
    }

    @Override
    public List<Ticket> listTickets() {
        List<Ticket> allTickets = new ArrayList<>();

        for (Cashier cashier : this.map.values()) {
            allTickets.addAll(cashier.getTicketList());
        }

        return allTickets;
    }


}

