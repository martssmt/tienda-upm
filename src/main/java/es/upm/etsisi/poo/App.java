package es.upm.etsisi.poo;

import es.upm.etsisi.poo.controller.*;
import es.upm.etsisi.poo.model.Catalog;

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
            CommandController commandController = new CommandController(productController, ticketController);
            boolean running = true;

            System.out.println("Welcome to the ticket module App.");
            System.out.println("Ticket module. Type 'help' to see commands.");

            while (running) {
                System.out.print("\ntUPM> ");
                String line = reader.readLine();
                running = commandController.parseCommand(line);
            }

            System.out.println("Closing application.");
            System.out.println("Goodbye!");

            reader.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}