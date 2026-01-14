package es.upm.etsisi.poo.app3.data.repositories;

import es.upm.etsisi.poo.app3.data.model.shop.products.Product;

public interface ProductRepository extends RepositoryShop<Product> {

    void add(Product product);

    boolean find(Product product);

}
