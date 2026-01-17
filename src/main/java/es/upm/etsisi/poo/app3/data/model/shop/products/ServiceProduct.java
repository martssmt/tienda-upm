package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.ServiceType;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.TicketItem;
import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * The {@code ServiceProduct} class represents a service that can be added to
 * company tickets in the store system.
 * <p>
 * Unlike standard products, service products do not have an intrinsic price
 * at ticket creation time. Their cost is calculated a posteriori (e.g. during
 * invoicing), therefore their unit price is always {@code 0} within the ticket.
 * </p>
 *
 * <p>
 * Each service has a {@link ServiceType} and a maximum usage date, which limits
 * its validity and determines whether it can be added to or kept in a ticket.
 * </p>
 *
 * <p>
 * This class directly fulfills the E3 requirement of handling services with
 * deferred pricing and usage constraints.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * ServiceProduct service =
 *     new ServiceProduct(LocalDate.now().plusDays(30), ServiceType.INSURANCE);
 * service.validateAvailability();
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 * @see Purchasable
 * @see ServiceType
 */
@Entity
@Table(name = "service_products")
public class ServiceProduct extends Purchasable<String> {

    /**
     * Maximum date on which the service can be used.
     */
    @Column(name = "max_usage_date")
    private LocalDate maxUsageDate;

    /**
     * Type of the service.
     */
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    /**
     * Protected no-argument constructor required by JPA.
     */
    protected ServiceProduct() {
        super();
    }

    /**
     * Creates a new service product with the given expiration date and type.
     *
     * @param maxUsageDate the maximum usage date of the service
     * @param serviceType  the service type
     */
    public ServiceProduct(LocalDate maxUsageDate, ServiceType serviceType) {
        this.maxUsageDate = maxUsageDate;
        this.serviceType = serviceType;
    }

    /**
     * Copy constructor.
     * <p>
     * Creates a new {@code ServiceProduct} by copying the state of an existing one.
     * </p>
     *
     * @param original the original service product to copy
     */
    public ServiceProduct(ServiceProduct original) {
        this.maxUsageDate = original.maxUsageDate;
        this.serviceType = original.serviceType;
        this.setId(original.getId());
    }

    /**
     * Returns the maximum usage date of the service.
     *
     * @return the maximum usage date
     */
    public LocalDate getMaxUsageDate() {
        return this.maxUsageDate;
    }

    /**
     * Returns the type of the service.
     *
     * @return the service type
     */
    public ServiceType getServiceType() {
        return this.serviceType;
    }

    /**
     * Assigns the identifier of the service.
     *
     * @param id the service identifier
     * @throws InvalidAttributeException if the identifier is {@code null}
     */
    @Override
    public void setId(String id) {
        if (id == null) {
            throw new InvalidAttributeException("Service id cannot be null");
        }
        this.id = id;
    }

    /**
     * Returns a string representation of the service product.
     *
     * @return a textual representation of the service product
     */
    @Override
    public String toString() {
        java.util.Date legacyDate = java.util.Date.from(this.maxUsageDate.atStartOfDay()
                .atZone(java.time.ZoneId.systemDefault())
                .toInstant());
        String cleanId = this.getId().replace("S", "");
        return "{class:ProductService, id:" + cleanId +
                ", category:" + this.serviceType +
                ", expiration:" + legacyDate + "}";
    }

    /**
     * Returns the unit price of the service in the context of a ticket item.
     * <p>
     * Service products do not have a price at ticket time, so this method
     * always returns {@code 0}.
     * </p>
     *
     * @param context the ticket item context
     * @return {@code 0}
     */
    @Override
    public double getUnitPrice(TicketItem context) {
        return 0;
    }

    /**
     * Validates the availability of the service.
     * <p>
     * A service is considered available only if its maximum usage date
     * has not been reached.
     * </p>
     *
     * @throws InvalidAttributeException if the service has expired
     */
    @Override
    public void validateAvailability() {
        if (this.maxUsageDate.isBefore(LocalDate.now())) {
            throw new InvalidAttributeException("Service expired");
        }
    }
}