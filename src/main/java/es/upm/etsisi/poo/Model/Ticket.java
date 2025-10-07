package es.upm.etsisi.poo.Model;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Iterator

public class Ticket {
    private final LinkedList<TicketItem> itemList;
    private final static int MAX_PRODUCTS = 100;
    private int numberOfProducts;

    public Ticket(){
        this.itemList = new LinkedList<>();
        this.numberOfProducts = 0;
    }

    public void addProduct(Product product, int quantity) {
        if(product == null){
            throw new NullPointerException("Product cannot be null");
        }

        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity cannot be negative or zero");
        }

        if(numberOfProducts >= MAX_PRODUCTS){
            throw new IllegalArgumentException("The number of products cannot be greater than " + MAX_PRODUCT);
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
            itemList.add(newItem);
        }

        numberOfProducts += quantity;
    }

    public boolean removeProduct(int id){
        boolean result = true;
        if(id < 0 || id >= this.itemList.size()){
            result = false;
        }
        itemList.remove(id);
        return result;
    }

    public void clear(){
        this.itemList.clear();
    }

    public double calculateTotalDiscount(){
        double result = 0;
        Iterator<TicketItem> iterator = this.itemList.iterator();
        while(iterator.hasNext()){
            TicketItem item = iterator.next();
            result += item.getDiscount();
        }
        return  result;
    }

    public double calculateTotalPrice(){
        double result = 0;
        Iterator<TicketItem> iterator = this.itemList.iterator();
        while(iterator.hasNext()){
            TicketItem item = iterator.next();
            result += item.getSubtotal();
        }
        return result;
    }

    public double calculateFinalPrice(){
        return this.calculateTotalPrice() -  calculateTotalDiscount();
    }

    public Product getProduct(int id){
        return this.itemList.get(id).getProduct();
    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();
        Iterator<TicketItem> iterator = this.itemList.iterator();
        while(iterator.hasNext()){
            TicketItem item = iterator.next();
            result.append(item.getProduct().toString());
            if(item.getDiscount() != 0) {
                result.append(" **discount -").append(item.getDiscount());
            }
            result.append("\n");
        }

        result.append("Total price: ").append(calculateTotalPrice()).append("\n");
        result.append("Total discount: ").append(calculateTotalDiscount()).append("\n");
        result.append("Final Price: ").append(calculateFinalPrice()).append("\n");

        return result.toString();
    }

}
