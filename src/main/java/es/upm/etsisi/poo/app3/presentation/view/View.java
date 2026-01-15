package es.upm.etsisi.poo.app3.presentation.view;

import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.data.model.user.User;

import java.util.Comparator;
import java.util.List;

public class View {

    public void show(String message) {
        System.out.println(message);
    }

    public void showClose() {
        System.out.println("Closing application.");
        System.out.println("Goodbye!");
    }

    public void showInit() {
        System.out.println("Welcome to the ticket module App.");
        System.out.println("Ticket module. Type 'help' to see commands.");
    }

    public void showCommandPrompt() {
        System.out.print("tUPM> ");
    }

    public void showError(String errorMessage) {
        System.out.println(errorMessage + "\n");
    }

    public <T> void showEntity(T item) {
        System.out.println(item);
    }

    public <T> void showList(String title, List<T> items) {
        this.show(title);
        if (!items.isEmpty()) {
            Comparator<T> comparator = null;
            T first = items.getFirst();
            if (first instanceof User) {
                comparator = (a, b) -> {
                    User u1 = (User) a;
                    User u2 = (User) b;
                    return u1.getName().compareToIgnoreCase(u2.getName());
                };
            } else if (first instanceof Product) {
                comparator = (a, b) -> {
                    Product p1 = (Product) a;
                    Product p2 = (Product) b;
                    return Integer.compare(p1.getIdAsInt(), p2.getIdAsInt());
                };
            }
            if (comparator != null)
                items.sort(comparator);
            for (T item : items) {
                System.out.println("\t" + item);
            }
        }
    }
}