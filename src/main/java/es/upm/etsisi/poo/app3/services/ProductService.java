package es.upm.etsisi.poo.app3.services;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.Category;
import es.upm.etsisi.poo.app3.data.model.shop.products.BasicProduct;
import es.upm.etsisi.poo.app3.data.model.shop.products.Product;
import es.upm.etsisi.poo.app3.data.repositories.ProductRepository;
import es.upm.etsisi.poo.app3.services.exceptions.DuplicateException;
import es.upm.etsisi.poo.app3.services.exceptions.NotFoundException;

import java.util.List;

public class ProductService implements Service<Product> {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void add(Product product, String id) {
        Integer idInteger = Integer.parseInt(id);
        if (this.productRepository.findById(idInteger) != null) {
            throw new DuplicateException("There is already a product with id " + id + " in the Catalog.");
        }
        this.productRepository.add(product, idInteger);
    }

    @Override
    public Product remove(String id) {
        Integer integerId = Integer.parseInt(id);
        Product product = this.productRepository.findById(integerId);
        if (product == null) {
            throw new NotFoundException("There is no product with id " + id + " in the Catalog.");
        }
        this.productRepository.remove(integerId);
        return product;
    }

    @Override
    public List<Product> list() {
        return this.productRepository.list();
    }

    public void add(Product product) {
        if (this.productRepository.find(product)) {
            throw new DuplicateException("There is already a product with this exact data in the Catalog.");
        }
        this.productRepository.add(product);
    }

    public Product update(String id, String field, String value) {
        Integer idInteger = Integer.parseInt(id);
        Product prod = findProd(idInteger);
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
        return prod;
    }

    public Product findProd(Integer id) {
        Product prod = this.productRepository.findById(id);
        if (prod == null) {
            throw new NotFoundException("There is no product with id " + id + " in the Catalog.");
        }
        return prod;
    }
}