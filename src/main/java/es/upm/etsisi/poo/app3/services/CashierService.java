package es.upm.etsisi.poo.app3.services;

import es.upm.etsisi.poo.app3.data.model.shop.products.CustomProduct;
import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;
import es.upm.etsisi.poo.app3.data.model.shop.products.ServiceProduct;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.data.model.user.Cashier;
import es.upm.etsisi.poo.app3.data.repositories.CashierRepository;
import es.upm.etsisi.poo.app3.services.exceptions.DuplicateException;
import es.upm.etsisi.poo.app3.services.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class CashierService implements Service<Cashier> {

    private final CashierRepository cashierRepository;

    public CashierService(CashierRepository cashierRepository) {
        this.cashierRepository = cashierRepository;
    }

    @Override
    public void add(Cashier cashier, String id) {
        if (this.cashierRepository.findById(id) != null) {
            throw new DuplicateException("There is already a cashier with id " + id + " registered.");
        }
        this.cashierRepository.add(cashier, id);
    }

    @Override
    public Cashier remove(String id) {
        Cashier cashier = this.cashierRepository.findById(id);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + id + " registered.");
        }
        this.cashierRepository.remove(id);
        return cashier;
    }

    @Override
    public List<Cashier> list() {
        return this.cashierRepository.list();
    }

    public void add(Cashier cashier) {
        if (this.cashierRepository.findByMail(cashier.getMail()) != null) {
            throw new DuplicateException("There is already a cashier with mail " + cashier.getMail() + " registered.");
        }
        this.cashierRepository.add(cashier);
    }

    public void newTicket(Ticket ticket, String cashierId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        cashier.newTicket(ticket);
    }

    public Ticket print(String cashierId, String ticketId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        cashier.closeTicket(ticketId);
        return cashier.getTicket(ticketId);
    }

    public Ticket addProduct(String cashierId, String ticketId, Purchasable purchasable, Integer quantity) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        cashier.addProduct(ticketId, purchasable, quantity);
        this.cashierRepository.add(cashier);
        return cashier.getTicket(ticketId);
    }

    public Ticket addCustomProduct(String cashierId, String ticketId, CustomProduct product, Integer quantity, String[] texts) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        cashier.addCustomProduct(ticketId, product, quantity, List.of(texts));
        this.cashierRepository.add(cashier);
        return cashier.getTicket(ticketId);
    }

    public Ticket removeProduct(String cashierId, String ticketId, Integer prodId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        cashier.removeProduct(ticketId, prodId);
        return cashier.getTicket(ticketId);
    }

    public List<String> ticketList() {
        ArrayList<String> tickets = new ArrayList<>();
        for (Ticket t : this.cashierRepository.listTickets())
            tickets.add(t.getName() + " - " + t.getStatus());
        return tickets;
    }

    public List<String> ticketListFromCashier(String cashierId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        ArrayList<String> tickets = new ArrayList<>();
        for (Ticket t : cashier.getTicketList())
            tickets.add(t.getName() + " -> " + t.getStatus());
        return tickets;
    }
}