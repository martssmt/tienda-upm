package es.upm.etsisi.poo.app3.data.model.shop.ticket;

import es.upm.etsisi.poo.app3.data.model.Entity;
import es.upm.etsisi.poo.app3.data.model.exceptions.FullTicketException;
import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.Category;
import es.upm.etsisi.poo.app3.data.model.shop.Status;
import es.upm.etsisi.poo.app3.data.model.shop.TicketType;
import es.upm.etsisi.poo.app3.data.model.shop.products.*;
import es.upm.etsisi.poo.app3.data.model.user.ClientType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@jakarta.persistence.Entity
@Table(name = "tickets")
public class Ticket extends Entity<String> {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id")
    @OrderBy("internalId ASC")
    private List<TicketItem> itemList;
    private static final Integer MAX_PRODUCTS = 100;
    private Integer numberOfProducts;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String name;
    @Enumerated(EnumType.STRING)
    private ClientType clientType;
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;
    @Transient // No se guarda en DB, es comportamiento
    private TicketPrintingStrategy printer;

    protected Ticket() {}

    public Ticket(String id, TicketType ticketType, ClientType clientType) {
        super();
        if (!id.matches("[0-9]{6}")) {
            throw new InvalidAttributeException("Invalid id");
        }
        this.id = id;
        this.itemList = new LinkedList<>();
        this.numberOfProducts = 0;
        this.status = Status.EMPTY;
        this.name = this.id;
        this.ticketType = ticketType;
        this.clientType = clientType;
        if (clientType == ClientType.PERSON && ticketType != TicketType.PRODUCT)
            throw new InvalidAttributeException("User tickets only accept product tickets");
        else if (clientType == ClientType.COMPANY && ticketType == TicketType.PRODUCT)
            throw new InvalidAttributeException("Company tickets do not accept only-products tickets");
        this.printer = null;
    }

    public Ticket(TicketType ticketType, ClientType clientType) {
        this(String.valueOf(new Random().nextInt(900000) + 100000), ticketType, clientType);
        this.name = this.generateName();
    }

    @PostLoad
    private void onLoad() {
        if (this.clientType == ClientType.COMPANY) {
            this.printer = new CompanyTicketPrinter();
        } else {
            this.printer = new PersonTicketPrinter();
        }
    }

    private String generateName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        String timestamp = LocalDateTime.now().format(formatter);
        return timestamp + "-" + this.id;
    }

    public List<TicketItem> getItemList() {
        return Collections.unmodifiableList(this.itemList);
    }

    public Integer getNumberOfProducts() {
        return this.numberOfProducts;
    }

    public Status getStatus() {
        return this.status;
    }

    public String getName() {
        return this.name;
    }

    public void setPrinter(TicketPrintingStrategy printer) {
        this.printer = printer;
    }

    public void add(Purchasable<?> purchasable, Integer quantity) {

        if (quantity <= 0) {
            throw new InvalidAttributeException("Quantity must be greater than 0");
        }

        boolean isService = purchasable instanceof ServiceProduct;

        validateType(isService);

        purchasable.validateAvailability();

        if (!isService && this.numberOfProducts + quantity > MAX_PRODUCTS) {
            throw new FullTicketException();
        }

        boolean itemFound = false;
        Iterator<TicketItem> iterator = this.itemList.iterator();
        while (iterator.hasNext() && !itemFound) {
            TicketItem item = iterator.next();
            if (item.getPurchasable().equals(purchasable)) {
                itemFound = true;
                item.setQuantity(item.getQuantity() + quantity);
            }
        }

        if (!itemFound) {
            TicketItem newItem;
            if (purchasable instanceof TimeProduct) {
                newItem = new TicketItem(new TimeProduct((TimeProduct) purchasable), quantity);
            } else if (isService) {
                newItem = new TicketItem(new ServiceProduct((ServiceProduct) purchasable), quantity);
                this.numberOfProducts += quantity;
            } else {
                newItem = new TicketItem(new BasicProduct((BasicProduct) purchasable), quantity);
                this.numberOfProducts += quantity;
            }
            this.itemList.add(newItem);
        }

        this.itemList.sort(null);

        if (this.status == Status.EMPTY) {
            this.status = Status.OPEN;
        }
    }

    private void validateType(boolean isService) {
        if (this.ticketType == TicketType.PRODUCT && isService) {
            throw new InvalidAttributeException("Product ticket cannot contain services");
        }

        if (this.ticketType == TicketType.SERVICE && !isService) {
            throw new InvalidAttributeException("Service ticket cannot contain products");
        }

        if (this.clientType == ClientType.PERSON && isService) {
            throw new InvalidAttributeException("User clients cannot add services");
        }
    }

    public void addCustom(CustomProduct product, Integer quantity, List<String> texts) {

        if (quantity <= 0) {
            throw new InvalidAttributeException("Quantity must be greater than 0");
        }

        if (ticketType == TicketType.SERVICE) {
            throw new InvalidAttributeException("Service ticket cannot contain products");
        }

        if (this.numberOfProducts + quantity > MAX_PRODUCTS) {
            throw new FullTicketException();
        }

        TicketItem newItem = new TicketItem(new CustomProduct(product), quantity, texts);
        this.itemList.add(newItem);
        this.numberOfProducts += quantity;

        if (status == Status.EMPTY) {
            status = Status.OPEN;
        }
    }

    public void remove(String purchasableId) {

        boolean itemFound = false;
        Iterator<TicketItem> iterator = this.itemList.iterator();
        while (iterator.hasNext() && !itemFound) {
            TicketItem item = iterator.next();
            if (item.getPurchasable().getId().equals(purchasableId)) {
                itemFound = true;
                if (!(item.getPurchasable() instanceof TimeProduct)) {
                    this.numberOfProducts -= item.getQuantity();
                }
                iterator.remove();
            }
        }

        if (this.itemList.isEmpty()) {
            this.status = Status.EMPTY;
        }
    }

    public void closeTicket() {
        if (this.status == Status.CLOSED) return;

        if (this.ticketType == TicketType.COMBINED) {
            boolean hasProduct = itemList.stream().anyMatch(item -> item.getPurchasable() instanceof Product);
            boolean hasService = itemList.stream().anyMatch(item -> item.getPurchasable() instanceof ServiceProduct);

            if (!hasProduct || !hasService) {
                throw new InvalidAttributeException("Combined ticket must contain at least one product and one service");
            }
        }

        for (TicketItem item : this.itemList) {
            item.getPurchasable().validateAvailability();
        }

        this.status = Status.CLOSED;
        LocalDateTime closeDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        String closeTimestamp = closeDate.format(formatter);
        this.name = this.id + "-" + closeTimestamp;
    }

    public double calculateCategoryDiscount() {
        double result = 0.0;

        Map<Category, Integer> quantitiesEachCategory = new HashMap<>();
        for (TicketItem item : this.itemList) {
            if (item.getPurchasable() instanceof BasicProduct) {
                Category category = ((BasicProduct) item.getPurchasable()).getCategory();
                int currentQuantity = quantitiesEachCategory.getOrDefault(category, 0);
                quantitiesEachCategory.put(category, currentQuantity + item.getQuantity());
            }
        }

        for (TicketItem item : this.itemList) {
            if (item.getPurchasable() instanceof BasicProduct) {
                Category category = ((BasicProduct) item.getPurchasable()).getCategory();
                int totalEachCategory = quantitiesEachCategory.get(category);
                if (totalEachCategory > 1) {
                    result += ((BasicProduct) item.getPurchasable()).getCategory().getDiscount();
                }
            }
        }

        return result;
    }

    public double calculateServiceDiscount() {
        if (this.clientType != ClientType.COMPANY || this.ticketType != TicketType.COMBINED) {
            return 0.0;
        }

        long numberOfServices = itemList.stream()
                .filter(item -> item.getPurchasable() instanceof ServiceProduct)
                .count();

        if (numberOfServices == 0) return 0.0;

        double totalProductsPrice = itemList.stream()
                .filter(item -> !(item.getPurchasable() instanceof ServiceProduct))
                .mapToDouble(TicketItem::getTotalPrice)
                .sum();

        return totalProductsPrice * (0.15 * numberOfServices);
    }

    public double calculateTotalDiscount() {
        return this.calculateCategoryDiscount() + this.calculateServiceDiscount();
    }

    public double calculateTotalPrice() {
        double result = 0.0;
        for (TicketItem item : this.itemList) {
            result += item.getTotalPrice();
        }
        return result;
    }

    public double calculateFinalPrice() {
        return Math.max(0.0, this.calculateTotalPrice() - this.calculateTotalDiscount());
    }

    @Override
    public void setId(String id) {
        if (!id.matches("[0-9]{6}")) {
            throw new InvalidAttributeException("Invalid id");
        }
        this.id = id;
    }

    @Override
    public String toString() {
        // Fallback de seguridad: si no hay printer, decidimos por tipo de cliente
        if (this.printer == null) {
            this.printer = (this.clientType == ClientType.COMPANY)
                    ? new CompanyTicketPrinter() : new PersonTicketPrinter();
        }
        return this.printer.format(this);
    }
}