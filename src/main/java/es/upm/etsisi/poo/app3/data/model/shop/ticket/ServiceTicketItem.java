package es.upm.etsisi.poo.app3.data.model.shop.ticket;

import es.upm.etsisi.poo.app3.data.model.shop.products.ProductService;

public class ServiceTicketItem extends TicketItem{
    public ServiceTicketItem(ProductService service){
        super(service, 1);
        service.validateUsage();
    }

    @Override
    public Double getTotalPrice() {
        return 0.0;
    }

    @Override
    public String toString(){
        ProductService s = (ProductService) purchasable;
        return "{class:ProductService, id:" + s.getId() +
                ", category:" + s.getServiceType() +
                ", expiration:" + s.getMaxUsageDate() + "}";
    }
}
