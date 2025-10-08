package es.upm.etsisi.poo.view;

import es.upm.etsisi.poo.model.Catalog;
import es.upm.etsisi.poo.model.Product;
import es.upm.etsisi.poo.model.Ticket;

public class ConsoleView {

    public static void showMessage(String message) {
        System.out.println(message);
    }

    public static void showHelp() {
        final String MESSAGE = """
        Commands:
         prod add <id> "<name>" <category> <price>
         prod list
         prod update <id> NAME|CATEGORY|PRICE <value>
         prod remove <id>
         ticket new
         ticket add <prodId> <quantity>
         ticket remove <prodId>
         ticket print
         echo "<texto>"
         help
         exit
        
        Categories: MERCH, STATIONERY, CLOTHES, BOOKS, ELECTRONICS
        Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOKS 10%, ELECTRONICS 3%.""";
        System.out.println(MESSAGE);
    }

    public static void showProduct(Product product) {
        if (product != null) {
            System.out.println(product);
        }
    }

    public static void showTicket(Ticket ticket) {
        System.out.println(ticket);
    }

    public static void showCatalog(Catalog catalog) {
        System.out.println(catalog);
    }
}