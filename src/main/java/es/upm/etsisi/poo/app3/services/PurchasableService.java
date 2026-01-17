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

/**
 * Service class responsible for managing {@link Purchasable} entities.
 * <p>
 * This service acts as an application-layer façade over the
 * {@link PurchasableRepository}, enforcing business rules related to
 * product and service management before delegating persistence operations.
 * </p>
 *
 * <p>
 * It supports adding, removing, listing, updating, and retrieving purchasable
 * elements, handling both products and services under a unified abstraction.
 * </p>
 *
 * @author Sofía
 * @version 3.0
 * @see Purchasable
 * @see Product
 * @see ServiceProduct
 * @see PurchasableRepository
 */
public class PurchasableService implements Service<Purchasable<Object>> {

    /**
     * Repository used to persist and retrieve purchasable elements.
     */
    private final PurchasableRepository purchasableRepository;

    /**
     * Creates a new service using the given purchasable repository.
     *
     * @param purchasableRepository the repository to use
     */
    public PurchasableService(PurchasableRepository purchasableRepository) {
        this.purchasableRepository = purchasableRepository;
    }

    /**
     * Adds a new purchasable element with an explicit identifier.
     * <p>
     * If an element with the same identifier already exists, a
     * {@link DuplicateException} is thrown.
     * </p>
     *
     * @param purchasable the purchasable element to add
     * @param id          the identifier to assign
     * @throws DuplicateException if an element with the given identifier already exists
     */
    @Override
    public void add(Purchasable purchasable, String id) {
        if (this.purchasableRepository.findById(id) != null) {
            throw new DuplicateException("There is already a product with id " + id + " in the Catalog.");
        }
        this.purchasableRepository.add(purchasable, id);
    }

    /**
     * Removes a purchasable element by its identifier.
     *
     * @param id the identifier of the element to remove
     * @return the removed purchasable element
     * @throws NotFoundException if no element with the given identifier exists
     */
    @Override
    public Purchasable remove(String id) {
        Purchasable purchasable = this.purchasableRepository.findById(id);
        if (purchasable == null) {
            throw new NotFoundException("There is no product with id " + id + " in the Catalog.");
        }
        this.purchasableRepository.remove(id);
        return purchasable;
    }

    /**
     * Retrieves all purchasable elements in the catalog.
     *
     * @return the list of purchasable elements
     */
    @Override
    public List<Purchasable<Object>> list() {
        return this.purchasableRepository.list();
    }

    /**
     * Adds a new purchasable element with automatically generated identifier.
     * <p>
     * Duplicate-content validation is intentionally omitted to comply with
     * input test requirements.
     * </p>
     *
     * @param purchasable the purchasable element to add
     */
    public void add(Purchasable purchasable) {
        this.purchasableRepository.add(purchasable);
    }

    /**
     * Updates a product attribute by identifier.
     * <p>
     * Only product entities can be updated. Service products are excluded
     * from updates. Supported fields are:
     * <ul>
     *   <li>{@code NAME}</li>
     *   <li>{@code CATEGORY} (only for {@link BasicProduct} and subclasses)</li>
     *   <li>{@code PRICE}</li>
     * </ul>
     * </p>
     *
     * @param id    the identifier of the product
     * @param field the field to update
     * @param value the new value
     * @return the updated product
     * @throws NotFoundException         if the product does not exist or is a service
     * @throws InvalidAttributeException if the field is invalid or unsupported
     */
    public Product update(String id, String field, String value) {
        Purchasable purchasable = findProd(id);

        if (purchasable instanceof ServiceProduct) {
            throw new NotFoundException(
                    "There is no product with id " + id + " in the Catalog. Services cannot be updated"
            );
        }

        Product prod = (Product) purchasable;

        switch (field.toUpperCase()) {
            case "NAME":
                prod.setName(value);
                break;

            case "CATEGORY":
                if (!(prod instanceof BasicProduct)) {
                    throw new InvalidAttributeException(
                            "Only BasicProduct or CustomProduct have category as a field"
                    );
                }
                ((BasicProduct) prod).setCategory(Category.valueOf(value.toUpperCase()));
                break;

            case "PRICE":
                prod.setPrice(Double.parseDouble(value));
                break;

            default:
                throw new InvalidAttributeException("Field not recognised");
        }

        this.purchasableRepository.update((Purchasable) prod);
        return prod;
    }

    /**
     * Finds a purchasable element by its identifier.
     *
     * @param id the identifier of the element
     * @return the found purchasable element
     * @throws NotFoundException if no element with the given identifier exists
     */
    public Purchasable findProd(String id) {
        Purchasable purchasable = this.purchasableRepository.findById(id);
        if (purchasable == null) {
            throw new NotFoundException("There is no product with id " + id + " in the Catalog.");
        }
        return purchasable;
    }
}