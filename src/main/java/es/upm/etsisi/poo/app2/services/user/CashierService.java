package es.upm.etsisi.poo.app2.services.user;

import es.upm.etsisi.poo.app2.data.model.shop.Product;
import es.upm.etsisi.poo.app2.data.model.shop.Ticket;
import es.upm.etsisi.poo.app2.data.model.user.Cashier;
import es.upm.etsisi.poo.app2.data.repositories.CashierRepository;
import es.upm.etsisi.poo.app2.presentation.view.View;
import es.upm.etsisi.poo.app2.services.Service;
import es.upm.etsisi.poo.app2.services.exceptions.DuplicateException;
import es.upm.etsisi.poo.app2.services.exceptions.NotFoundException;

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
            throw new NotFoundException("There is no cashier with id " + id + " registered.");
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

    public void newTicket(Ticket ticket, String cashierId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        cashier.newTicket(Ticket);
    }

    public void newTicket(Ticket ticket, String ticketId, String cashierId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        if (cashier.getTicket(ticketId) != null) {
            throw new DuplicateException("There is already a ticket with id " + ticketId + "registered by cashier with id " + cashierId + ".")
        }
        cashier.newTicket(ticket, ticketId);
    }

    public void print(String cashierId, String ticketId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        if (cashier.getTicket(ticketId) == null) {
            throw new NotFoundException("There is no ticket with id " + ticketId + " registered by cashier with id " + cashierId + ".");
        }
        cashier.closeTicket(ticketId);
        this.view.showEntity(cashier.getTicket(ticketId));
    }

    public void addProduct(String cashierId, String ticketId, Product product, Integer quantity) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        Ticket ticket = cashier.getTicket(ticketId);
        if (ticket == null) {
            throw new NotFoundException("There is no ticket with id " + ticketId + " registered by cashier with id " + cashierId + ".");
        }
        ticket.add(product, quantity);
    }

    public void removeProduct(String cashierId, String ticketId, String prodId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        Ticket ticket = cashier.getTicket(ticketId);
        if (ticket == null) {
            throw new NotFoundException("There is no ticket with id " + ticketId + " registered by cashier with id " + cashierId + ".");
        }
        ticket.remove(prodId);
    }

    public void ticketList() {
        this.view.showList("Ticket List:", this.cashierRepository.listTickets());
    }

    public void ticketListFromCashier(String cashierId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        this.view.showList("Tickets:", cashier.getTicketList());
    }

}
