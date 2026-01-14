package es.upm.etsisi.poo.app3.data.repositories;

import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;

public interface PurchasableRepository extends RepositoryShop<Purchasable<Object>, Object> {

    void add(Purchasable<Object> purchasable);

    boolean find(Purchasable<Object> purchasable);

}
