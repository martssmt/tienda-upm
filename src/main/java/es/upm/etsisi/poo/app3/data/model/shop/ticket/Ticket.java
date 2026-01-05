package es.upm.etsisi.poo.app3.data.model.shop.ticket;

import es.upm.etsisi.poo.app3.data.model.Entity;
import es.upm.etsisi.poo.app3.data.model.exceptions.FullTicketException;
import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.Category;
import es.upm.etsisi.poo.app3.data.model.shop.Status;
import es.upm.etsisi.poo.app3.data.model.shop.TicketType;
import es.upm.etsisi.poo.app3.data.model.shop.products.*;
import es.upm.etsisi.poo.app3.data.model.user.ClientType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Ticket extends Entity<String> {
    private final List<TicketItem> itemList;
    private static final Integer MAX_PRODUCTS = 100;
    private Integer numberOfProducts;
    private final String clientId;
    private final String cashierId;
    private Status status;
    private String name;
    private final ClientType clientType;
    private final TicketType ticketType;

    public Ticket(String id, String clientId, String cashierId, TicketType ticketType, ClientType clientType) {
        super();
        if (!id.matches("[0-9]{6}")) {
            throw new InvalidAttributeException("Invalid id");
        }
        this.id = id;
        if (clientId.length() != 9) {
            throw new InvalidAttributeException("Invalid clientId");
        }
        this.clientId = clientId;
        this.cashierId = cashierId;
        this.itemList = new LinkedList<>();
        this.numberOfProducts = 0;
        this.status = Status.EMPTY;
        this.name = this.id;
        this.ticketType = ticketType;
        this.clientType = clientType;
    }

    public Ticket(String clientId, String cashierId, TicketType ticketType, ClientType clientType) {
        this(String.valueOf(new Random().nextInt(900000) + 100000), clientId, cashierId, ticketType, clientType);
        this.name = this.generateName();
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

    public String getClientId() {
        return this.clientId;
    }

    public String getCashierId() {
        return this.cashierId;
    }

    public Status getStatus() {
        return this.status;
    }

    public String getName() {
        return this.name;
    }

    public void add(Purchasable<?> purchasable, Integer quantity) {

        if (quantity <= 0) {
            throw new InvalidAttributeException("Quantity must be greater than 0");
        }

        boolean isService = purchasable instanceof ProductService;

        if (this.ticketType == TicketType.PRODUCT && isService) {
            throw new InvalidAttributeException("Product ticket cannot contain services");
        }

        if (this.ticketType == TicketType.SERVICE && !isService) {
            throw new InvalidAttributeException("Service ticket cannot contain products");
        }

        if (this.clientType == ClientType.PERSON && isService) {
            throw new InvalidAttributeException("User clients cannot add services");
        }

        if (!isService && this.numberOfProducts + quantity > MAX_PRODUCTS) {
            throw new FullTicketException();
        }

        if(purchasable instanceof TimeProduct) {
            ((TimeProduct) purchasable).validateAvailability();
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
            if(isService){
                newItem = new ServiceTicketItem((ProductService) purchasable);
            } else if (purchasable instanceof BasicProduct) {
                newItem = new BasicTicketItem((BasicProduct) purchasable, quantity, ((BasicProduct) purchasable).getCategory().getDiscount());
                this.numberOfProducts += quantity;
            } else {
                newItem = new TimeTicketItem((TimeProduct) purchasable, quantity);
                this.numberOfProducts += quantity;
            }
            this.itemList.add(newItem);
        }

        this.itemList.sort(null);

        if (this.status == Status.EMPTY) {
            this.status = Status.OPEN;
        }
    }

    public void addCustom(Product product, Integer quantity, String[] texts) {

        if (quantity <= 0) {
            throw new InvalidAttributeException("Quantity must be greater than 0");
        }

        if (ticketType == TicketType.SERVICE) {
            throw new InvalidAttributeException("Service ticket cannot contain products");
        }

        if (this.numberOfProducts + quantity > MAX_PRODUCTS) {
            throw new FullTicketException();
        }
        TicketItem newItem = new CustomTicketItem((CustomProduct) product, quantity, ((CustomProduct) product).getCategory().getDiscount(), texts);
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
                if(!(item.getPurchasable() instanceof ProductService)){
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
        if (this.status == Status.CLOSED) {
            return;
        }

        boolean hasProduct = false;
        boolean hasService = false;

        for (TicketItem item : this.itemList) {
            Purchasable<?> purchasable = item.getPurchasable();
            if(purchasable instanceof ProductService){
                hasService = true;
                ((ProductService) purchasable).validateUsage();
            }else{
                hasProduct = true;
            }

            if (purchasable instanceof TimeProduct) {
                ((TimeProduct) purchasable).validateAvailability();
            }
        }

        if(ticketType == TicketType.COMBINED && (!hasService || !hasProduct)) {
            throw new InvalidAttributeException("Combined ticket must contain at least one product and one service");
        }

        this.status = Status.CLOSED;
        LocalDateTime closeDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        String closeTimestamp = closeDate.format(formatter);
        this.id = this.id + "-" + closeTimestamp;
        this.name = this.id;
    }

    private double calculateCategoryDiscount() {
        double result = 0.0;

        Map<Category, Integer> quantitiesEachCategory = new HashMap<>();
        for (TicketItem item : this.itemList) {
            if (item instanceof BasicTicketItem) {
                Category category = ((BasicProduct) item.getPurchasable()).getCategory();
                int currentQuantity = quantitiesEachCategory.getOrDefault(category, 0);
                quantitiesEachCategory.put(category, currentQuantity + item.getQuantity());
            }
        }

        for (TicketItem item : this.itemList) {
            if (item instanceof BasicTicketItem) {
                Category category = ((BasicProduct) item.getPurchasable()).getCategory();
                int totalEachCategory = quantitiesEachCategory.get(category);
                if (totalEachCategory > 1) {
                    result += ((BasicTicketItem) item).getDiscount();
                }
            }
        }

        return result;
    }

    private double calculateServiceDiscount(){
        double result = 0.0;
        int numberOfServices = 0;
        for(TicketItem item : this.itemList){
            if(item.getPurchasable() instanceof ProductService){
                numberOfServices++;
            }
        }

        if(numberOfServices > 0){
            result = this.calculateTotalPrice() * 0.15 *  numberOfServices;
        }

        return result;
    }

    private double calculateTotalDiscount(){
        return this.calculateCategoryDiscount() + this.calculateServiceDiscount();
    }

    private double calculateTotalPrice() {
        double result = 0.0;
        for (TicketItem item : this.itemList) {
            result += item.getTotalPrice();
        }
        return result;
    }

    private double calculateFinalPrice() {
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
        StringBuilder result = new StringBuilder("Ticket: " + this.name + "\n");

        int numberOfServices = 0;
        int numberOfProducts = 0;
        for (TicketItem item : this.itemList) {
            if(item.getPurchasable() instanceof ProductService){
                numberOfServices++;
            }else{
                numberOfProducts++;
            }
        }

        if (numberOfServices > 0) {
            result.append("Services Included: \n");
            for (TicketItem item : itemList) {
                if (item.getPurchasable() instanceof ProductService) {
                    result.append("  ").append(item).append("\n");
                }
            }
        }

        if (numberOfProducts > 0) {
            result.append("Product Included: \n");

            Map<Category, Integer> quantitiesEachCategory = new HashMap<>();
            for (TicketItem item : this.itemList) {
                if (item instanceof BasicTicketItem) {
                    Category category = ((BasicProduct) item.getPurchasable()).getCategory();
                    int currentQuantity = quantitiesEachCategory.getOrDefault(category, 0);
                    quantitiesEachCategory.put(category, currentQuantity + item.getQuantity());
                }
            }

            List<TicketItem> sortedItems = new ArrayList<>(this.itemList);
            sortedItems.sort(Comparator.comparing(item -> ((Product)item.getPurchasable()).getName()));

            for (TicketItem item : sortedItems) {
                if (item instanceof TimeTicketItem) {
                    result.append("\t").append(item);
                    result.append("\n");
                }

                if (item instanceof BasicTicketItem) {
                    Category category = ((BasicProduct) item.getPurchasable()).getCategory();

                    double perUnitPrice = item.getTotalPrice() / item.getQuantity();
                    double discountEachProduct = perUnitPrice * category.getDiscount();

                    for (int i = 0; i < item.getQuantity(); i++) {
                        result.append("\t").append(item);
                        if (quantitiesEachCategory.get(category) > 1 && discountEachProduct > 0) {
                            result.append(" **discount -").append(Math.floor(discountEachProduct * 1000) / 1000.0);
                        }
                        result.append("\n");
                    }
                }
            }
        }

        if (numberOfProducts > 0) {

            double totalPrice = Math.floor(calculateTotalPrice() * 1000) / 1000.0;
            double serviceDiscount = Math.floor(calculateServiceDiscount() * 1000) / 1000.0;
            double totalDiscount = Math.floor(calculateTotalDiscount() * 1000) / 1000.0;
            double finalPrice = Math.floor(calculateFinalPrice() * 1000) / 1000.0;

            result.append("  Total price: ").append(totalPrice).append("\n");

            if (numberOfServices > 0 && serviceDiscount > 0) {
                result.append("  Extra Discount from services:")
                        .append(serviceDiscount)
                        .append(" **discount -")
                        .append(serviceDiscount)
                        .append("\n");
            }

            result.append("  Total discount: ").append(totalDiscount).append("\n");
            result.append("  Final Price: ").append(finalPrice);
        }

        return result.toString();
    }
}