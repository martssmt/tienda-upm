package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.ServiceType;
import es.upm.etsisi.poo.app3.data.model.shop.ticket.TicketItem;

import java.time.LocalDate;

public class ServiceProduct extends Purchasable<String> {

    private final LocalDate maxUsageDate;
    private final ServiceType serviceType;
    private static int idCounter = 1;

    public ServiceProduct(LocalDate maxUsageDate, ServiceType serviceType) {
        this.maxUsageDate = maxUsageDate;
        this.serviceType = serviceType;
        this.setId(idCounter++ + "S");
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
    public void setId(String id){
        if(id == null){
            throw new InvalidAttributeException("Service id cannot be null");
        }
        this.id = id;
    }

    @Override
    public String toString(){
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
        if(this.maxUsageDate.isBefore(LocalDate.now())){
            throw new InvalidAttributeException("Service expired");
        }
    }
}
