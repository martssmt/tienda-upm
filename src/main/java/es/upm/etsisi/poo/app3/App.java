package es.upm.etsisi.poo.app3;

import es.upm.etsisi.poo.app3.DependencyInjector;

public class App {
    public static void main(String[] args) {
        DependencyInjector.getInstance().run(args);
    }
}
