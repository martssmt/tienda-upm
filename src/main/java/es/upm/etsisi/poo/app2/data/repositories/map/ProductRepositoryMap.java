package es.upm.etsisi.poo.app2.data.repositories.map;

import es.upm.etsisi.poo.app2.data.model.shop.products.Product;
import es.upm.etsisi.poo.app2.data.repositories.ProductRepository;

import java.util.Iterator;

public class ProductRepositoryMap extends RepositoryShopMap<Product> implements ProductRepository {

    public ProductRepositoryMap() {
        super();
    }

    @Override
    public void add(Product product) {
        while (this.map.containsKey(this.id)) {
            this.id++;
        }
        product.setId(id);
        this.map.put(id, product);
        this.id++;
    }

    @Override
    public boolean find(Product product) {
        boolean found = false;
        Iterator<Product> iterator = this.map.values().iterator();
        while (iterator.hasNext() && !found) {
            Product actualProduct = iterator.next();
            if (product.equalsWithoutId(actualProduct)) found = true;
        }
        return found;
    }

}

