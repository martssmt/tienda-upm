package es.upm.etsisi.poo.app2.services.user;

import es.upm.etsisi.poo.app2.data.model.shop.Ticket;
import es.upm.etsisi.poo.app2.data.model.shop.TicketItem;
import es.upm.etsisi.poo.app2.data.model.user.Cashier;
import es.upm.etsisi.poo.app2.data.repositories.CashierRepository;
import es.upm.etsisi.poo.app2.presentation.view.View;
import es.upm.etsisi.poo.app2.services.Service;
import es.upm.etsisi.poo.app2.services.exceptions.DuplicateException;

public class CashierService implements Service<Cashier> {

    private final CashierRepository cashierRepository;
    private final View view;

    public CashierService(CashierRepository cashierRepository, View view) {
        this.cashierRepository = cashierRepository;
        this.view = view;
    }

    @Override
    public void add(Cashier cashier, String id) {
        if (this.cashierRepository.findById(id) != null) {
            throw new DuplicateException("There is already a cashier with id " + id + " registered.");
        }
        this.cashierRepository.add(cashier, id);
    }

    @Override
    public void remove(String id) {
        if (this.cashierRepository.findById(id) != null) {
            throw new DuplicateException("There is already a cashier with id " + id + " registered.");
        }
        this.cashierRepository.remove(id);
    }

    @Override
    public void list() {
        this.view.showList("Cash:", this.cashierRepository.list());
    }

    public void add(Cashier cashier) {
        if (this.cashierRepository.findByMail(cashier.getMail()) != null) {
            throw new DuplicateException("There is already a cashier with mail " + cashier.getMail() + " registered.");
        }
        this.cashierRepository.add(cashier);
    }

    public void newTicket(Ticket ticket) {

    }

    public void newTicket(Ticket ticket, String id) {

    }

    public void print(String cashierId, String ticketId) {

    }

    public void addProduct(String cashierId, String ticketId, TicketItem ticketItem) {

    }

    public void removeProduct(String cashierId, String ticketId, String prodId) {

    }

    public void ticketList() {
        this.view.showList("Ticket List:", this.cashierRepository.listTickets());
    }

    public void ticketListFromCashier(String cashierId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        this.view.showList("Tickets:", cashier.getTicketList());
    }

}
