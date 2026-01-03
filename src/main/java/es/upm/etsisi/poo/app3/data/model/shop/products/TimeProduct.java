package es.upm.etsisi.poo.app3.data.model.shop.products;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.TimeProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TimeProduct extends Product {
    private final LocalDate openDate;
    private static final Integer MAX_PEOPLE_GLOBAL = 100;
    private final Integer maxPeople;
    private final TimeProductType type;
    private final Integer planningHours;

    public TimeProduct(String name, TimeProductType type, Double price, LocalDate openDate, Integer maxPeople) {
        super(name, price);
        this.type = type;
        this.openDate = openDate;
        if (maxPeople > MAX_PEOPLE_GLOBAL) {
            throw new InvalidAttributeException("Error adding product");
        }
        this.maxPeople = maxPeople;
        this.planningHours = type.getPlanningHours();
        LocalDateTime minAllowedDate = LocalDateTime.now().plusHours(this.planningHours);
        LocalDateTime openingDateTime = this.openDate.atStartOfDay();
        if (openingDateTime.isBefore(minAllowedDate)) {
            throw new InvalidAttributeException("Error adding product");
        }
    }

    public LocalDate getOpenDate() {
        return this.openDate;
    }

    public static Integer getMaxPeopleGlobal() {
        return MAX_PEOPLE_GLOBAL;
    }

    public TimeProductType getType() {
        return this.type;
    }

    public Integer getPlanningHours() {
        return this.planningHours;
    }

    public Integer getMaxPeople() {
        return maxPeople;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        String productType;
        if (type == TimeProductType.MEETING) {
            productType = "Meeting";
        } else {
            productType = "Food";
        }

        stringBuilder.append("{class:").append(productType)
                .append(", id:").append(this.getId())
                .append(", name:'").append(this.getName()).append("'")
                .append(", price:").append(this.getPrice())
                .append(", date of Event:");

        if (this.openDate == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(openDate);
        }

        stringBuilder.append(", max people allowed:");
        stringBuilder.append(MAX_PEOPLE_GLOBAL);

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    public void validateAvailability(){
        LocalDateTime minDate = LocalDateTime.now().plusHours(this.planningHours);
        if(openDate.atStartOfDay().isBefore(minDate)){
            throw new InvalidAttributeException("Invalid product date");
        }
    }
}
