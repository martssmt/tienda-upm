package es.upm.etsisi.poo.app3.data.model.shop;

public enum TimeProductType {
    FOOD(72),
    MEETING(12);

    private final Integer planningHours;

    TimeProductType(Integer planningHours) {
        this.planningHours = planningHours;
    }

    public Integer getPlanningHours() {
        return this.planningHours;
    }
}