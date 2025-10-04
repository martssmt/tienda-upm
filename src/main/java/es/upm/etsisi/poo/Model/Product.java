package es.upm.etsisi.poo.Model;

public class Product {
    private int id;
    private String name;
    private Category category;
    private double price;

    public Product(int id, String name, Category category, double price) {
        this.id=id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(double price) {
       this.price=price;
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
        return this.id==product.id;
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
        return "{class:Product, id:"+this.id+", name:'"+this.name+"'," +
                " category:"+this.category.name()+", price:"+this.price+"}";
    }
}
