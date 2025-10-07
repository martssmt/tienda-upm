package es.upm.etsisi.poo;

import es.upm.etsisi.poo.Controller.*;
import es.upm.etsisi.poo.Model.Catalog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            ProductController productController = new ProductController();
            Catalog catalog = productController.getCatalog();
            TicketController ticketController = new TicketController(catalog);
            CommandController command = new CommandController(productController, ticketController);
            System.out.println("Welcome to the ticket module App.");
            System.out.println("Ticket module. Type 'help' to see commands.");
            while (true) {
                System.out.print("tUPM> ");
                String line = reader.readLine();
                command.parseCommand(line);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}