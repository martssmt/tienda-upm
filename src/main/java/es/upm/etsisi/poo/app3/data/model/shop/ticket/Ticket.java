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
    private Status status;
    private String name;
    private final ClientType clientType;
    private final TicketType ticketType;

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
        if (clientType == ClientType.PERSON &&  ticketType != TicketType.PRODUCT)
            throw new InvalidAttributeException("User tickets only accept product tickets");
        else if (clientType == ClientType.COMPANY &&  ticketType == TicketType.PRODUCT)
            throw new InvalidAttributeException("Company tickets do not accept only-products tickets");
    }

    public Ticket(TicketType ticketType, ClientType clientType) {
        this(String.valueOf(new Random().nextInt(900000) + 100000), ticketType, clientType);
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

        boolean isService = purchasable instanceof ServiceProduct;

        validateType(purchasable, isService);

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
            if(purchasable instanceof TimeProduct) {
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

    private void validateType(Purchasable<?>  purchasable, boolean isService) {
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
                if(!(item.getPurchasable() instanceof TimeProduct)){
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
            if(purchasable instanceof ServiceProduct){
                hasService = true;
                purchasable.validateAvailability();
            } else{
                hasProduct = true;
            }

            if (purchasable instanceof TimeProduct) {
                purchasable.validateAvailability();
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

    private double calculateServiceDiscount(){
        double result = 0.0;
        int numberOfServices = 0;
        for(TicketItem item : this.itemList){
            if(item.getPurchasable() instanceof ServiceProduct){
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
            if(item.getPurchasable() instanceof ServiceProduct){
                numberOfServices++;
            }else{
                numberOfProducts++;
            }
        }

        if (numberOfServices > 0) {
            result.append("Services Included: \n");
            for (TicketItem item : itemList) {
                if (item.getPurchasable() instanceof ServiceProduct) {
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