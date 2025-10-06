package es.upm.etsisi.poo.Model;

import java.util.HashMap;
import java.util.Map;

public class Catalog {
    private static final int MAX_PRODUCTS = 200;
    private final Map<Integer, Product> productsList;

    public Catalog() {
        this.productsList = new HashMap<>();
    }

    public Map<Integer, Product> getProductList() { // no se usa
        return new HashMap<>(this.productsList);
    }

    public Product getProduct(int id) {
        return this.productsList.get(id);
    }

    public boolean addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("The product cannot be null");
        }

        if (this.productsList.size() >= MAX_PRODUCTS) {
            //System.out.println("Lista de productos completa, no se pueden añadir mas productos");
            return false;
        }

        if (this.productsList.containsKey(product.getId())) {
            //System.out.println("No puede haber un producto con un mismo id");
            return false;
        }

        this.productsList.put(product.getId(), product);
        return true;
    }

    public boolean removeProduct(int id) {
        if (this.productsList.containsKey(id)) {
            this.productsList.remove(id);
            return true;
        }
        //System.out.println("No existe en la lista el producto seleccionado");
        return false;
    }

    public boolean updateProduct(int id, String field, String value) {
        Product product = productsList.get(id);
        if (product == null) {
            return false; // No existe el producto seleccionado
        }

        switch (field.toLowerCase()) {
            case "name":
                product.setName(value);
                return true;

            case "category":
                try {
                    product.setCategory(Category.valueOf(value.toUpperCase()));
                    return true;
                } catch (IllegalArgumentException e) {
                    return false; // Category no existe
                }

            case "price":
                try {
                    double newPrice = Double.parseDouble(value);
                    product.setPrice(newPrice);
                    return true;
                } catch (NumberFormatException e) {
                    return false; // Error precio no es un numero
                }

            default:
                return false; // field no encontrado
        }
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("Catalog:\n");
        for(Product product : this.productsList.values()){
            sb.append(product);
        }
        return sb.toString();
    }
}