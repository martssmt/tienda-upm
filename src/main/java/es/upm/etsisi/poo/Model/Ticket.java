package es.upm.etsisi.poo.Model;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private final LinkedList<TicketItem> itemList;
    private final static int MAX_PRODUCTS = 100;
    private int numberOfProducts;

    public Ticket(){
        this.itemList = new LinkedList<>();
        this.numberOfProducts = 0;
    }

    public void addProduct(Product product, int quantity) throws IllegalArgumentException, NullPointerException {
        if(product == null){
            throw new NullPointerException("Product cannot be null");
        }

        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity cannot be negative or zero");
        }

        if(this.numberOfProducts >= MAX_PRODUCTS){
            throw new IllegalArgumentException("The number of products cannot be greater than " + MAX_PRODUCTS);
        }

        boolean itemFound = false;

        Iterator<TicketItem> iterator = this.itemList.iterator();
        while(iterator.hasNext() && !itemFound){
            TicketItem item = iterator.next();
            if(item.getProduct().equals(product)){
                itemFound = true;
                item.setQuantity(item.getQuantity() + quantity);
            }
        }
        if(!itemFound){
            TicketItem newItem = new TicketItem(product, quantity, product.getCategory().getDiscount());
            this.itemList.add(newItem);
        }

        this.numberOfProducts += quantity;
        this.itemList.sort(null);
    }

    public void removeProduct(Product product) throws IllegalArgumentException {
        boolean itemFound = false;
        Iterator<TicketItem> iterator = this.itemList.iterator();
        while(iterator.hasNext() && !itemFound){
            TicketItem item = iterator.next();
            if (item.getProduct().equals(product)){
                itemFound = true;
                this.numberOfProducts -= item.getQuantity();
                this.itemList.remove(item);
            }
        }
        if(!itemFound){
            throw new IllegalArgumentException("Product is not included in the ticket");
        }
    }

    public void clear(){
        this.itemList.clear();
    }

    private double calculateTotalDiscount(){
        double result = 0;

        Map<Category, Integer> QuantitiesEachCategory = new HashMap<>();
        for(TicketItem item : this.itemList){
            Category category = item.getProduct().getCategory();
            int currentQuantity = QuantitiesEachCategory.getOrDefault(category, 0);
            QuantitiesEachCategory.put(category, currentQuantity + item.getQuantity());
        }

        for(TicketItem item : this.itemList){
            Category category = item.getProduct().getCategory();
            int totalEachCategory =  QuantitiesEachCategory.get(category);

            if(totalEachCategory > 1){
                result += item.getDiscount();
            }
        }
        return  result;
    }

    private double calculateTotalPrice(){
        double result = 0;
        for (TicketItem item : this.itemList) {
            result += item.getSubtotal();
        }
        return result;
    }

    private double calculateFinalPrice(){
        return this.calculateTotalPrice() -  this.calculateTotalDiscount();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        Map<Category, Integer> QuantitiesEachCategory = new HashMap<>();
        for(TicketItem item : this.itemList){
            Category category = item.getProduct().getCategory();
            int currentQuantity = QuantitiesEachCategory.getOrDefault(category, 0);
            QuantitiesEachCategory.put(category, currentQuantity + item.getQuantity());
        }

        for(TicketItem item : this.itemList){
            double DiscountEachProduct = item.getProduct().getPrice() * item.getProduct().getCategory().getDiscount();
            Category category = item.getProduct().getCategory();

            for(int i = 0; i < item.getQuantity(); i++){
                result.append(item.getProduct().toString());
                if(QuantitiesEachCategory.get(category) > 1 && DiscountEachProduct > 0){
                    result.append("  **discount -").append(DiscountEachProduct);
                }
                result.append("\n");
            }
        }

        result.append("Total price: ").append(this.calculateTotalPrice()).append("\n");
        result.append("Total discount: ").append(this.calculateTotalDiscount()).append("\n");
        result.append("Final Price: ").append(this.calculateFinalPrice()).append("\n");

        return result.toString();
    }

}
