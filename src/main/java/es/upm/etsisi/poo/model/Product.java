package es.upm.etsisi.poo.model;

public class Product {
    private final int id;
    private String name;
    private Category category;
    private double price;

    public Product(int id, String name, Category category, double price)
            throws IllegalArgumentException {
        if (name == null || name.isEmpty() || name.length() > 100) {
            throw new IllegalArgumentException("The name field cannot be empty or exceed 100 characters");
        }
        if (price <= 0) throw new IllegalArgumentException("The price field must be greater than 0");

        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty() || name.length() > 100) {
            throw new IllegalArgumentException("The name field cannot be empty or exceed 100 characters");
        }
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(double price) throws IllegalArgumentException {
        if (price <= 0) throw new IllegalArgumentException("The price field must be greater than 0");
        this.price = price;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Category getCategory() {
        return this.category;
    }

    public double getPrice() {
        return this.price;
    }

    private boolean equals(Product product) {
        return this.id == product.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            return this.equals((Product) obj);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "{class:Product, id:" + this.id + ", name:'" + this.name + "'," +
                " category:" + this.category.name() + ", price:" + this.price + "}";
    }
}