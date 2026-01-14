package es.upm.etsisi.poo.app3;

import es.upm.etsisi.poo.app3.data.repositories.CashierRepository;
import es.upm.etsisi.poo.app3.data.repositories.ClientRepository;
import es.upm.etsisi.poo.app3.data.repositories.hibernate.JPAUtil;
import es.upm.etsisi.poo.app3.data.repositories.ProductRepository;
import es.upm.etsisi.poo.app3.data.repositories.map.CashierRepositoryMap;
import es.upm.etsisi.poo.app3.data.repositories.map.ClientRepositoryMap;
import es.upm.etsisi.poo.app3.data.repositories.map.ProductRepositoryMap;
import es.upm.etsisi.poo.app3.presentation.cli.CommandLineInterface;
import es.upm.etsisi.poo.app3.presentation.cli.ErrorHandler;
import es.upm.etsisi.poo.app3.presentation.cli.commands.Echo;
import es.upm.etsisi.poo.app3.presentation.cli.commands.Exit;
import es.upm.etsisi.poo.app3.presentation.cli.commands.Help;
import es.upm.etsisi.poo.app3.presentation.cli.commands.product.*;
import es.upm.etsisi.poo.app3.presentation.cli.commands.ticket.*;
import es.upm.etsisi.poo.app3.presentation.cli.commands.user.*;
import es.upm.etsisi.poo.app3.presentation.view.View;
import es.upm.etsisi.poo.app3.services.CashierService;
import es.upm.etsisi.poo.app3.services.ClientService;
import es.upm.etsisi.poo.app3.services.ProductService;

public class DependencyInjector {
    private static final DependencyInjector instance = new DependencyInjector();

    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final CashierRepository cashierRepository;

    private final ClientService clientService;
    private final CashierService cashierService;
    private final ProductService productService;

    private final ErrorHandler errorHandler;
    private final View view;
    private final CommandLineInterface commandLineInterface;

    private DependencyInjector() {
        productRepository = new ProductRepositoryMap();
        clientRepository = new ClientRepositoryMap();
        cashierRepository = new CashierRepositoryMap();

        clientService = new ClientService(this.clientRepository);
        cashierService = new CashierService(this.cashierRepository);
        productService = new ProductService(this.productRepository);

        this.view = new View();
        this.commandLineInterface = new CommandLineInterface(this.view);

        // commands.user
        this.commandLineInterface.add(new ClientAdd(this.view, this.clientService));
        this.commandLineInterface.add(new ClientRemove(this.view, this.clientService));
        this.commandLineInterface.add(new ClientList(this.view, this.clientService));
        this.commandLineInterface.add(new CashAdd(this.view, this.cashierService));
        this.commandLineInterface.add(new CashRemove(this.view, this.cashierService));
        this.commandLineInterface.add(new CashList(this.view, this.cashierService));
        this.commandLineInterface.add(new CashTickets(this.view, this.cashierService));
        // commands.ticket
        this.commandLineInterface.add(new TicketNew(this.view, this.cashierService, this.clientService));
        this.commandLineInterface.add(new TicketAdd(this.view, this.cashierService, this.productService));
        this.commandLineInterface.add(new TicketRemove(this.view, this.cashierService));
        this.commandLineInterface.add(new TicketPrint(this.view, this.cashierService));
        this.commandLineInterface.add(new TicketList(this.view, this.cashierService));
        // commands.product
        this.commandLineInterface.add(new ProdAdd(this.view, this.productService));
        this.commandLineInterface.add(new ProdUpdate(this.view, this.productService));
        this.commandLineInterface.add(new ProdAddFood(this.view, this.productService));
        this.commandLineInterface.add(new ProdAddMeeting(this.view, this.productService));
        this.commandLineInterface.add(new ProdList(this.view, this.productService));
        this.commandLineInterface.add(new ProdRemove(this.view, this.productService));
        // commands
        this.commandLineInterface.add(new Help(this.commandLineInterface));
        this.commandLineInterface.add(new Echo(this.view));
        this.commandLineInterface.add(new Exit());

        this.errorHandler = new ErrorHandler();
    }

    public static DependencyInjector getInstance() {
        return DependencyInjector.instance;
    }

    public void run(String[] args) {
        try {
            this.errorHandler.handlesErrors(this.commandLineInterface, this.view, args);
        } finally {
            JPAUtil.shutdown();
        }
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public View getView() {
        return view;
    }

    public CommandLineInterface getCommandLineInterface() {
        return commandLineInterface;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public CashierService getCashierService() {
        return cashierService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public ClientRepository getClientRepository() {
        return clientRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public CashierRepository getCashierRepository() {
        return cashierRepository;
    }
}