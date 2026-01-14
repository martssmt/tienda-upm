package es.upm.etsisi.poo.app3.data.repositories.map;

import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;
import es.upm.etsisi.poo.app3.data.repositories.PurchasableRepository;

import java.util.Iterator;

public class PurchasableRepositoryMap extends RepositoryShopMap<Purchasable<?>> implements PurchasableRepository {

    public PurchasableRepositoryMap() {
        super();
    }

    @Override
    public void add(Purchasable<?> purchasable) {
        while (this.map.containsKey(this.id)) {
            this.id++;
        }
        purchasable.setId(this.id);
        this.map.put(id, product);
        this.id++;
    }

    @Override
    public boolean find(Purchasable<?> purchasable) {
        boolean found = false;
        Iterator<Product> iterator = this.map.values().iterator();
        while (iterator.hasNext() && !found) {
            Product actualProduct = iterator.next();
            if (product.equalsWithoutId(actualProduct)) found = true;
        }
        return found;
    }


}
