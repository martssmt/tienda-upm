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

/**
 * The {@code Ticket} class represents a persistent purchase ticket within the store system.
 * <p>
 * A ticket aggregates a list of {@link TicketItem} entries and enforces business rules
 * regarding the allowed item types (products and/or services) depending on the
 * {@link TicketType} and the {@link ClientType}. Tickets also manage their lifecycle
 * through a {@link Status} state machine (EMPTY, OPEN, CLOSED).
 * </p>
 *
 * <p>
 * A ticket provides pricing and discount calculation, including category-based discounts
 * and company-specific service discounts. Ticket formatting is delegated to a
 * {@link TicketPrintingStrategy} (Strategy design pattern), allowing different output
 * representations depending on the client and ticket type.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * Ticket ticket = new Ticket(TicketType.COMBINED, ClientType.COMPANY);
 * ticket.add(product, 2);
 * ticket.add(service, 1);
 * ticket.closeTicket();
 * System.out.println(ticket); // uses printing strategy
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 * @see TicketItem
 * @see TicketPrintingStrategy
 * @see TicketType
 * @see ClientType
 * @see Status
 */
@jakarta.persistence.Entity
@Table(name = "tickets")
public class Ticket extends Entity<String> {

    /**
     * List of ticket items contained in the ticket.
     * <p>
     * Items are persisted and ordered by {@code internalId} to preserve insertion order
     * semantics for printing and traceability.
     * </p>
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id")
    @OrderBy("internalId ASC")
    private List<TicketItem> itemList;

    /**
     * Maximum number of products allowed in a ticket.
     */
    private static final Integer MAX_PRODUCTS = 100;

    /**
     * Current amount of product units in the ticket.
     * <p>
     * This counter is used to enforce the {@link #MAX_PRODUCTS} limit. Time products are
     * not counted as standard products, as their constraints are validated by availability rules.
     * </p>
     */
    private Integer numberOfProducts;

    /**
     * Current lifecycle status of the ticket.
     */
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * Human-friendly name of the ticket (often includes timestamp information).
     */
    private String name;

    /**
     * Client type associated with the ticket.
     */
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    /**
     * Ticket type defining whether it contains products, services, or both.
     */
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    /**
     * Printing strategy used to format the ticket output.
     * <p>
     * Marked as transient because it represents behavior, not persisted state.
     * </p>
     */
    @Transient
    private TicketPrintingStrategy printer;

    /**
     * Protected no-argument constructor required by JPA.
     */
    protected Ticket() {}

    /**
     * Creates a ticket with an explicit identifier, ticket type, and client type.
     * <p>
     * The ticket identifier must be a 6-digit numeric string. The constructor also validates
     * compatibility rules between {@link ClientType} and {@link TicketType}.
     * </p>
     *
     * @param id         the ticket identifier (6 digits)
     * @param ticketType the ticket type (PRODUCT, SERVICE, or COMBINED)
     * @param clientType the client type (PERSON or COMPANY)
     * @throws InvalidAttributeException if the identifier format is invalid or if the
     *                                   client/ticket combination is not allowed
     */
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

    /**
     * Creates a ticket with an auto-generated identifier.
     * <p>
     * The identifier is randomly generated as a 6-digit numeric string and the ticket name
     * is initialized with a timestamp-based label.
     * </p>
     *
     * @param ticketType the ticket type (PRODUCT, SERVICE, or COMBINED)
     * @param clientType the client type (PERSON or COMPANY)
     * @throws InvalidAttributeException if the client/ticket combination is not allowed
     */
    public Ticket(TicketType ticketType, ClientType clientType) {
        this(String.valueOf(new Random().nextInt(900000) + 100000), ticketType, clientType);
        this.name = this.generateName();
    }

    /**
     * Initializes non-persistent state after the entity is loaded from the database.
     * <p>
     * This method reinstates the appropriate {@link TicketPrintingStrategy} based on
     * the {@link ClientType}, because strategies are not persisted.
     * </p>
     */
    @PostLoad
    private void onLoad() {
        if (this.clientType == ClientType.COMPANY) {
            this.printer = new CompanyTicketPrinter();
        } else {
            this.printer = new PersonTicketPrinter();
        }
    }

    /**
     * Generates a human-friendly name for the ticket, including a timestamp and identifier.
     *
     * @return the generated ticket name
     */
    private String generateName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        String timestamp = LocalDateTime.now().format(formatter);
        return timestamp + "-" + this.id;
    }

    /**
     * Returns an unmodifiable view of the ticket items.
     *
     * @return the list of ticket items
     */
    public List<TicketItem> getItemList() {
        return Collections.unmodifiableList(this.itemList);
    }

    /**
     * Returns the current number of product units in the ticket.
     *
     * @return the number of product units
     */
    public Integer getNumberOfProducts() {
        return this.numberOfProducts;
    }

    /**
     * Returns the current ticket status.
     *
     * @return the ticket status
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * Returns the ticket name.
     *
     * @return the ticket name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Injects a printing strategy for ticket formatting.
     * <p>
     * This method supports dependency injection of different printing behaviors.
     * </p>
     *
     * @param printer the printing strategy to use
     */
    public void setPrinter(TicketPrintingStrategy printer) {
        this.printer = printer;
    }

    /**
     * Adds a purchasable item to the ticket.
     * <p>
     * The ticket validates item availability and enforces type constraints based on
     * {@link TicketType} and {@link ClientType}. If the item already exists in the ticket,
     * its quantity is increased. Otherwise, a new {@link TicketItem} is created.
     * </p>
     *
     * @param purchasable the item to add (product or service)
     * @param quantity    the quantity to add
     * @throws InvalidAttributeException if quantity is not positive or if the ticket
     *                                   type does not allow the item
     * @throws FullTicketException       if adding products exceeds the maximum allowed
     */
    public void add(Purchasable<?> purchasable, Integer quantity) {

        if (quantity != null && quantity <= 0) {
            throw new InvalidAttributeException("Quantity must be greater than 0");
        }

        boolean isService = purchasable instanceof ServiceProduct;

        validateType(isService);

        purchasable.validateAvailability();

        if (quantity != null && this.numberOfProducts + quantity > MAX_PRODUCTS) {
            throw new FullTicketException();
        }

        boolean itemFound = false;
        Iterator<TicketItem> iterator = this.itemList.iterator();
        while (quantity != null && iterator.hasNext() && !itemFound) {
            TicketItem item = iterator.next();
            if (item.getPurchasable().equals(purchasable)) {
                itemFound = true;
                item.setQuantity(item.getQuantity() + quantity);
            }
        }

        if (!itemFound) {
            TicketItem newItem;
            if (purchasable instanceof BasicProduct) {
                newItem = new TicketItem((BasicProduct) purchasable, quantity);
                newItem.setSalePrice(purchasable.getUnitPrice(newItem));
                newItem.setFrozenString(purchasable.toString());
                this.numberOfProducts += quantity;
            } else if (isService) {
                newItem = new TicketItem((ServiceProduct) purchasable);
                newItem.setSalePrice(0.0);
                newItem.setFrozenString(purchasable.toString());
            } else { // instanceof TimeProduct
                newItem = new TicketItem(purchasable, quantity);
                newItem.setSalePrice(purchasable.getUnitPrice(newItem));
                newItem.setFrozenString(purchasable.toString());
                this.numberOfProducts += quantity;
            }
            this.itemList.add(newItem);
        }

        this.itemList.sort(null);

        if (this.status == Status.EMPTY) {
            this.status = Status.OPEN;
        }
    }

    /**
     * Validates whether the ticket can accept an item of the given kind (service or product).
     *
     * @param isService {@code true} if the item is a service, {@code false} if it is a product
     * @throws InvalidAttributeException if the item is not allowed by the ticket rules
     */
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

    /**
     * Adds a custom product to the ticket.
     * <p>
     * Custom products are treated as products and are not allowed in service-only tickets.
     * The maximum product constraint also applies.
     * </p>
     *
     * @param product  the custom product
     * @param quantity the quantity to add
     * @param texts    custom text fragments associated with the product
     * @throws InvalidAttributeException if quantity is not positive or if the ticket type
     *                                   forbids adding products
     * @throws FullTicketException       if adding the custom product exceeds the maximum
     */
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

        TicketItem newItem = new TicketItem(product, quantity, texts);
        newItem.setSalePrice(product.getUnitPrice(newItem));
        newItem.setFrozenString(product.toString());

        this.itemList.add(newItem);
        this.numberOfProducts += quantity;

        if (status == Status.EMPTY) {
            status = Status.OPEN;
        }
    }

    /**
     * Removes an item from the ticket by purchasable identifier.
     * <p>
     * If the last item is removed, the ticket status becomes {@link Status#EMPTY}.
     * </p>
     *
     * @param purchasableId the purchasable identifier to remove
     */
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

    /**
     * Closes the ticket, preventing any further modifications.
     * <p>
     * For combined tickets, at least one product and one service must be present.
     * Before closing, availability is revalidated for all items.
     * </p>
     *
     * @throws InvalidAttributeException if combined ticket constraints are not satisfied
     */
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

    /**
     * Calculates the category-based discount for the ticket.
     * <p>
     * A category discount is applied when there is more than one unit of the same
     * {@link Category} present across basic products.
     * </p>
     *
     * @return the category discount amount
     */
    public double calculateCategoryDiscount() {
        double result = 0.0;

        Map<Category, Integer> quantitiesEachCategory = new HashMap<>();
        for (TicketItem item : this.itemList) {
            if (item.getPurchasable() instanceof BasicProduct bp) {
                Category category = bp.getCategory();
                quantitiesEachCategory.put(category, quantitiesEachCategory.getOrDefault(category, 0) + item.getQuantity());
            }
        }

        for (TicketItem item : this.itemList) {
            if (item.getPurchasable() instanceof BasicProduct bp) {
                Category category = bp.getCategory();
                if (quantitiesEachCategory.get(category) > 1) {
                    result += item.getTotalDiscount();
                }
            }
        }

        return result;
    }

    /**
     * Calculates the company service discount for combined tickets.
     * <p>
     * When the ticket belongs to a company client and is of type {@link TicketType#COMBINED},
     * an additional discount is applied to the total product price:
     * 15% per service contracted.
     * </p>
     *
     * @return the service discount amount (0.0 if not applicable)
     */
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

    /**
     * Calculates the total discount of the ticket.
     *
     * @return the total discount
     */
    public double calculateTotalDiscount() {
        return this.calculateCategoryDiscount() + this.calculateServiceDiscount();
    }

    /**
     * Calculates the total price of the ticket before discounts.
     *
     * @return the total price
     */
    public double calculateTotalPrice() {
        double result = 0.0;
        for (TicketItem item : this.itemList) {
            result += item.getTotalPrice();
        }
        return result;
    }

    /**
     * Calculates the final price of the ticket after discounts.
     *
     * @return the final price (never negative)
     */
    public double calculateFinalPrice() {
        return Math.max(0.0, this.calculateTotalPrice() - this.calculateTotalDiscount());
    }

    /**
     * Assigns the identifier of the ticket.
     *
     * @param id the ticket identifier (6 digits)
     * @throws InvalidAttributeException if the identifier format is invalid
     */
    @Override
    public void setId(String id) {
        if (!id.matches("[0-9]{6}")) {
            throw new InvalidAttributeException("Invalid id");
        }
        this.id = id;
    }

    /**
     * Returns a formatted string representation of the ticket.
     * <p>
     * Formatting is delegated to the configured {@link TicketPrintingStrategy}.
     * If no strategy is present (e.g. after manual construction), a fallback strategy
     * is selected based on the {@link ClientType}.
     * </p>
     *
     * @return a formatted representation of the ticket
     */
    @Override
    public String toString() {
        // Safety fallback: if printer is not set, choose based on client type
        if (this.printer == null) {
            this.printer = (this.clientType == ClientType.COMPANY)
                    ? new CompanyTicketPrinter() : new PersonTicketPrinter();
        }
        return this.printer.format(this);
    }
}