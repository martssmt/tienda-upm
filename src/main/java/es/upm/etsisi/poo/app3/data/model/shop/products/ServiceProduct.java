package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.ServiceType;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.TicketItem;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "service_products")
public class ServiceProduct extends Purchasable<String> {

    @Column(name = "max_usage_date")
    private LocalDate maxUsageDate;
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    // Hibernate necesita este constructor
    protected ServiceProduct() {
        super();
    }

    public ServiceProduct(LocalDate maxUsageDate, ServiceType serviceType, Integer nextId) {
        this.maxUsageDate = maxUsageDate;
        this.serviceType = serviceType;
        this.id = nextId.toString() + "S";
    }

    public ServiceProduct(ServiceProduct original) {
        this.maxUsageDate = original.maxUsageDate;
        this.serviceType = original.serviceType;
        this.setId(original.getId());
    }

    public LocalDate getMaxUsageDate() {
        return this.maxUsageDate;
    }

    public ServiceType getServiceType() {
        return this.serviceType;
    }

    @Override
    public void setId(String id) {
        if (id == null) {
            throw new InvalidAttributeException("Service id cannot be null");
        }
        this.id = id;
    }

    @Override
    public String toString() {
        return "{class:ProductService, id:" + this.getId() +
                ", category:" + this.serviceType +
                ", expiration:" + this.maxUsageDate + "}";
    }

    @Override
    public double getUnitPrice(TicketItem context) {
        return 0;
    }

    @Override
    public void validateAvailability() {
        if (this.maxUsageDate.isBefore(LocalDate.now())) {
            throw new InvalidAttributeException("Service expired");
        }
    }
}
