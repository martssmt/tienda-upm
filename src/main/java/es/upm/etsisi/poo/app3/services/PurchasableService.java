package es.upm.etsisi.poo.app3.services;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.Category;
import es.upm.etsisi.poo.app3.data.model.shop.products.BasicProduct;
import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.data.model.shop.products.Purchasable;
import es.upm.etsisi.poo.app3.data.model.shop.products.ServiceProduct;
import es.upm.etsisi.poo.app3.data.repositories.PurchasableRepository;
import es.upm.etsisi.poo.app3.services.exceptions.DuplicateException;
import es.upm.etsisi.poo.app3.services.exceptions.NotFoundException;

import java.util.List;

public class PurchasableService implements Service<Purchasable<Object>> {

    private final PurchasableRepository purchasableRepository;

    public PurchasableService(PurchasableRepository purchasableRepository) {
        this.purchasableRepository = purchasableRepository;
    }

    @Override
    public void add(Purchasable purchasable, String id) {
        Integer idInteger = Integer.parseInt(id);
        if (this.purchasableRepository.findById(idInteger) != null) {
            throw new DuplicateException("There is already a product with id " + id + " in the Catalog.");
        }
        this.purchasableRepository.add(purchasable, idInteger);
    }

    @Override
    public Purchasable remove(String id) {
        Purchasable purchasable = this.purchasableRepository.findById(id);
        if (purchasable == null) {
            throw new NotFoundException("There is no product with id " + id + " in the Catalog.");
        }
        this.purchasableRepository.remove(id);
        return purchasable;
    }

    @Override
    public List<Purchasable<Object>> list() {
        return this.purchasableRepository.list();
    }

    public void add(Purchasable purchasable) {
        if (this.purchasableRepository.find(purchasable)) {
            throw new DuplicateException("There is already a product with this exact data in the Catalog.");
        }
        this.purchasableRepository.add(purchasable);
    }

    public Product update(String id, String field, String value) {
        Integer idInteger = Integer.parseInt(id);
        Purchasable purchasable = findProd(idInteger);
        if (purchasable instanceof ServiceProduct) {
            throw new NotFoundException("There is no product with id " + id + " in the Catalog. Services cannot be updated");
        }
        Product prod = (Product) purchasable;
        switch (field.toUpperCase()) {
            case "NAME":
                prod.setName(value);
                break;
            case "CATEGORY":
                if (!(prod instanceof BasicProduct)) {
                    throw new InvalidAttributeException("Only BasicProduct or CustomProduct have category as a field");
                }
                ((BasicProduct) prod).setCategory(Category.valueOf(value.toUpperCase()));
                break;
            case "PRICE":
                prod.setPrice(Double.parseDouble(value));
                break;
            default:
                throw new InvalidAttributeException("Field not recognised");
        }
        this.purchasableRepository.add((Purchasable) prod);
        return prod;
    }

    public Purchasable findProd(String id) {
        Purchasable purchasable = this.purchasableRepository.findById(id);
        if (purchasable == null) {
            throw new NotFoundException("There is no product with id " + id + " in the Catalog.");
        }
        return purchasable;
    }
}