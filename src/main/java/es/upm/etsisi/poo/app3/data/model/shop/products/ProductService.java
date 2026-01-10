package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.ServiceType;

import java.time.LocalDate;

public class ProductService extends Purchasable<String> {

    private final LocalDate maxUsageDate;
    private final ServiceType serviceType;
    private static int idCounter = 1;

    public ProductService(LocalDate maxUsageDate, ServiceType serviceType) {
        this.maxUsageDate = maxUsageDate;
        this.serviceType = serviceType;
        this.setId(idCounter++ + "S");
    }

    public ProductService(ProductService original) {
        this.maxUsageDate = original.maxUsageDate;
        this.serviceType = original.serviceType;
        this.setId(original.getId());
    }

    public void validateUsage(){
        if(this.maxUsageDate.isBefore(LocalDate.now())){
            throw new InvalidAttributeException("Service expired");
        }
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
}
