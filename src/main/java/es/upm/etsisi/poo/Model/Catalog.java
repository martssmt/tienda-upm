package es.upm.etsisi.poo.Model;

import java.util.HashMap;
import java.util.Map;

public class Catalog {
    private static final int MAX_PRODUCTS = 200;
    private final Map<Integer, Product> productsList;

    public Catalog() {
        this.productsList = new HashMap<>();
    }

    public boolean addProduct(Product product) {

        if (this.productsList.size()>=MAX_PRODUCTS){
            return false;
        }

        if (this.productsList.containsKey(product.getId())){
            return false;
        }

        this.productsList.put(product.getId(), product);
        return true;
    }
}
