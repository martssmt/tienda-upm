package es.upm.etsisi.poo.app2.data.model.shop.ticket;

import es.upm.etsisi.poo.app2.data.model.Entity;
import es.upm.etsisi.poo.app2.data.model.exceptions.FullTicketException;
import es.upm.etsisi.poo.app2.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app2.data.model.shop.Category;
import es.upm.etsisi.poo.app2.data.model.shop.Status;
import es.upm.etsisi.poo.app2.data.model.shop.products.BasicProduct;
import es.upm.etsisi.poo.app2.data.model.shop.products.CustomProduct;
import es.upm.etsisi.poo.app2.data.model.shop.products.Product;
import es.upm.etsisi.poo.app2.data.model.shop.products.TimeProduct;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Ticket extends Entity<String> {
    private final List<TicketItem> itemList;
    private final Integer MAX_PRODUCTS = 100;
    private Integer numberOfProducts;
    private final String clientId;
    private final String cashierId;
    private Status status;
    private String name;

    private final LocalDateTime openDate;
    private LocalDateTime closeDate;

    public Ticket(String id, String clientId, String cashierId) {
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
        this.itemList = new LinkedList<TicketItem>();
        this.numberOfProducts = 0;
        this.status = Status.EMPTY;
        this.openDate = LocalDateTime.now();
        this.name = this.id;
    }

    public Ticket(String clientId, String cashierId) {
        this(String.valueOf(new Random().nextInt(900000) + 100000), clientId, cashierId);
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

    public void add(Product product, Integer quantity) {
        if (this.numberOfProducts + quantity > this.MAX_PRODUCTS) {
            throw new FullTicketException();
        }

        if (quantity <= 0) {
            throw new InvalidAttributeException("Quantity must be greater than 0");
        }

        boolean itemFound = false;
        Iterator<TicketItem> iterator = this.itemList.iterator();
        while (iterator.hasNext() && !itemFound) {
            TicketItem item = iterator.next();
            if (item.getProduct().equals(product)) {
                itemFound = true;
                item.setQuantity(item.getQuantity() + quantity);
            }
        }

        if (!itemFound) {
            TicketItem newItem;
            if (product instanceof BasicProduct) {
                newItem = new BasicTicketItem((BasicProduct) product, quantity, ((BasicProduct) product).getCategory().getDiscount());
            } else {
                newItem = new TimeTicketItem((TimeProduct) product, quantity);
            }
            this.itemList.add(newItem);
        }

        this.numberOfProducts += quantity;
        this.itemList.sort(null);

        if (this.status == Status.EMPTY) {
            this.status = Status.OPEN;
        }
    }

    public void addCustom(Product product, Integer quantity, String[] texts) {
        if (this.numberOfProducts + quantity > this.MAX_PRODUCTS) {
            throw new FullTicketException();
        }
        TicketItem newItem = new CustomTicketItem((CustomProduct) product, quantity, ((CustomProduct) product).getCategory().getDiscount(), texts);
        this.itemList.add(newItem);
        this.numberOfProducts += quantity;

        if (status == Status.EMPTY) {
            status = Status.OPEN;
        }
    }

    public void remove(Integer productId) {
        boolean itemFound = false;
        Iterator<TicketItem> iterator = this.itemList.iterator();
        while (iterator.hasNext() && !itemFound) {
            TicketItem item = iterator.next();
            if (item.getProduct().getId().equals(productId)) {
                itemFound = true;
                this.numberOfProducts -= item.getQuantity();
                this.itemList.remove(item);
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
        this.status = Status.CLOSED;
        this.closeDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        String closeTimestamp = this.closeDate.format(formatter);
        this.id = this.id + "-" + closeTimestamp;
        this.name = this.id;
    }

    private double calculateTotalDiscount() {
        double result = 0.0;

        Map<Category, Integer> quantitiesEachCategory = new HashMap<>();
        for (TicketItem item : this.itemList) {
            if (item instanceof BasicTicketItem) {
                Category category = ((BasicProduct) item.getProduct()).getCategory();
                int currentQuantity = quantitiesEachCategory.getOrDefault(category, 0);
                quantitiesEachCategory.put(category, currentQuantity + item.getQuantity());
            }
        }

        for (TicketItem item : this.itemList) {
            if (item instanceof BasicTicketItem) {
                Category category = ((BasicProduct) item.getProduct()).getCategory();
                int totalEachCategory = quantitiesEachCategory.get(category);
                if (totalEachCategory > 1) {
                    result += ((BasicTicketItem) item).getDiscount();
                }
            }
        }

        return result;
    }

    private double calculateTotalPrice() {
        double result = 0.0;
        for (TicketItem item : this.itemList) {
            result += item.getTotalPrice();
        }
        return result;
    }

    private double calculateFinalPrice() {
        return this.calculateTotalPrice() - this.calculateTotalDiscount();
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
        Map<Category, Integer> quantitiesEachCategory = new HashMap<>();
        for (TicketItem item : this.itemList) {
            if (item instanceof BasicTicketItem) {
                Category category = ((BasicProduct) item.getProduct()).getCategory();
                int currentQuantity = quantitiesEachCategory.getOrDefault(category, 0);
                quantitiesEachCategory.put(category, currentQuantity + item.getQuantity());
            }
        }

        List<TicketItem> sortedItems = new ArrayList<>(this.itemList);
        sortedItems.sort(Comparator.comparing(item -> item.getProduct().getName()));

        for (TicketItem item : sortedItems) {
            if (item instanceof TimeTicketItem) {
                result.append("\t").append(item);
                result.append("\n");
            }

            if (item instanceof BasicTicketItem) {
                Category category = ((BasicProduct) item.getProduct()).getCategory();

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
        result.append("\tTotal price: ").append(Math.floor(this.calculateTotalPrice() * 1000) / 1000.0).append("\n");
        result.append("\tTotal discount: ").append(Math.floor(this.calculateTotalDiscount() * 1000) / 1000.0).append("\n");
        result.append("\tFinal Price: ").append(Math.floor(this.calculateFinalPrice() * 1000) / 1000.0);

        return result.toString();
    }
}