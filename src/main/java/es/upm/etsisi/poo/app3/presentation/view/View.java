package es.upm.etsisi.poo.app3.presentation.view;

import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;
import es.upm.etsisi.poo.app3.data.model.user.User;

import java.util.Comparator;
import java.util.List;

/**
 * The {@code View} class represents the presentation layer of the application
 * for command-line interaction.
 * <p>
 * This class is responsible for displaying messages, errors, entities, and
 * lists of domain objects to the standard output. It centralizes all user-facing
 * output in order to keep presentation concerns separated from business logic.
 * </p>
 *
 * <p>
 * The view supports generic rendering of entities and lists, applying
 * type-specific sorting rules when displaying collections of users or
 * purchasable elements.
 * </p>
 *
 * @author Tomás
 * @version 3.0
 * @see User
 * @see Purchasable
 */
public class View {

    /**
     * Displays a generic message to the standard output.
     *
     * @param message the message to display
     */
    public void show(String message) {
        System.out.println(message);
    }

    /**
     * Displays the application closing message.
     */
    public void showClose() {
        System.out.println("Closing application.");
        System.out.println("Goodbye!");
    }

    /**
     * Displays the application initialization message.
     */
    public void showInit() {
        System.out.println("Welcome to the ticket module App.");
        System.out.println("Ticket module. Type 'help' to see commands.");
    }

    /**
     * Displays the command prompt.
     */
    public void showCommandPrompt() {
        System.out.print("tUPM> ");
    }

    /**
     * Displays an error message.
     *
     * @param errorMessage the error message to display
     */
    public void showError(String errorMessage) {
        System.out.println(errorMessage + "\n");
    }

    /**
     * Displays a single entity using its {@code toString()} representation.
     *
     * @param item the entity to display
     * @param <T>  the entity type
     */
    public <T> void showEntity(T item) {
        System.out.println(item);
    }

    /**
     * Displays a list of entities with a title.
     * <p>
     * If the list contains {@link User} instances, they are sorted by name
     * (case-insensitive). If it contains {@link Purchasable} instances, they
     * are sorted by identifier before being displayed.
     * </p>
     *
     * @param title the title to display before the list
     * @param items the list of items to display
     * @param <T>   the type of the items
     */
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
            } else if (first instanceof Purchasable) {
                comparator = (a, b) -> {
                    Purchasable p1 = (Purchasable) a;
                    Purchasable p2 = (Purchasable) b;
                    return ((String) p1.getId()).compareTo((String) p2.getId());
                };
            }

            if (comparator != null) {
                items.sort(comparator);
            }

            for (T item : items) {
                System.out.println("\t" + item);
            }
        }
    }
}