package es.upm.etsisi.poo.app3.services;

import es.upm.etsisi.poo.app3.data.model.shop.products.CustomProduct;
import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.data.model.user.Cashier;
import es.upm.etsisi.poo.app3.data.model.user.Client;
import es.upm.etsisi.poo.app3.data.repositories.CashierRepository;
import es.upm.etsisi.poo.app3.data.repositories.ClientRepository;
import es.upm.etsisi.poo.app3.data.repositories.PurchasableRepository;
import es.upm.etsisi.poo.app3.services.exceptions.DuplicateException;
import es.upm.etsisi.poo.app3.services.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class CashierService implements Service<Cashier> {

    private final CashierRepository cashierRepository;
    private final ClientRepository clientRepository;

    public CashierService(CashierRepository cashierRepository, ClientRepository  clientRepository) {
        this.cashierRepository = cashierRepository;
        this.clientRepository = clientRepository;
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

    public void newTicket(Ticket ticket, String cashierId, String clientId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        Client client = this.clientRepository.findById(clientId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        if (client == null) {
            throw new NotFoundException("There is no client with id " + clientId + " registered.");
        }
        client.addTicket(ticket.getId());
        cashier.newTicket(ticket);
        this.cashierRepository.update(cashier);
        this.clientRepository.update(client);
    }

    public Ticket print(String cashierId, String ticketId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        cashier.closeTicket(ticketId);
        this.cashierRepository.update(cashier);
        return cashier.getTicket(ticketId);
    }

    public Ticket addProduct(String cashierId, String ticketId, Purchasable purchasable, Integer quantity) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        cashier.addProduct(ticketId, purchasable, quantity);
        this.cashierRepository.update(cashier);
        return cashier.getTicket(ticketId);
    }

    public Ticket addCustomProduct(String cashierId, String ticketId, CustomProduct product, Integer quantity, String[] texts) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        cashier.addCustomProduct(ticketId, product, quantity, List.of(texts));
        this.cashierRepository.update(cashier);
        return cashier.getTicket(ticketId);
    }

    public Ticket removeProduct(String cashierId, String ticketId, String prodId) {
        Cashier cashier = this.cashierRepository.findById(cashierId);
        if (cashier == null) {
            throw new NotFoundException("There is no cashier with id " + cashierId + " registered.");
        }
        cashier.removeProduct(ticketId, prodId);
        this.cashierRepository.update(cashier);
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