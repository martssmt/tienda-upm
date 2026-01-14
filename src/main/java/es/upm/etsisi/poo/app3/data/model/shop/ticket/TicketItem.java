package es.upm.etsisi.poo.app3.data.model.shop.ticket;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import es.upm.etsisi.poo.app3.data.model.shop.products.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ticket_items")
public class TicketItem implements Comparable<TicketItem> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internalId; // ID técnico para base de datos
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchasable_id")
    private Purchasable purchasable;
    @Column(name = "quantity")
    private int quantity;
    private double discountApplied;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ticket_item_customTexts", joinColumns = @JoinColumn(name = "ticket_item_id")) // Para guardar los textos de CustomProduct si fuera necesario
    private List<String> customTexts;

    protected TicketItem() {}

    public TicketItem(Purchasable purchasable, Integer quantity) {
        if (quantity <= 0) {
            throw new InvalidAttributeException("Quantity must be positive");
        }
        this.purchasable = purchasable;
        this.quantity = quantity;
        this.discountApplied = 0.0;
        this.customTexts = new ArrayList<>();
    }

    public TicketItem(BasicProduct purchasable, Integer quantity) {
        if (quantity <= 0) {
            throw new InvalidAttributeException("Quantity must be positive");
        }
        this.purchasable = purchasable;
        this.quantity = quantity;
        this.discountApplied = purchasable.getCategory().getDiscount();
        this.customTexts = new ArrayList<>();
    }

    public TicketItem(CustomProduct purchasable, Integer quantity, List<String> customTexts) {
        if (quantity <= 0) {
            throw new InvalidAttributeException("Quantity must be positive");
        }
        this.purchasable = purchasable;
        this.quantity = quantity;
        this.discountApplied = purchasable.getCategory().getDiscount();
        this.customTexts = customTexts;
    }

    public Double getTotalPrice() {
        return this.purchasable.getUnitPrice(this) * this.quantity;
    }

    public Double getDiscount() {
        return this.discountApplied * this.getTotalPrice();
    }

    public Purchasable<?> getPurchasable() {
        return this.purchasable;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<String> getCustomTexts() {
        return this.customTexts;
    }

    public void setCustomTexts(List<String> customTexts) {
        this.customTexts = customTexts;
    }

    public int getCustomTextsSize() {
        return this.customTexts.size();
    }

    @Override
    public String toString() {
        return this.purchasable.toString();
    }

    @Override
    public int compareTo(TicketItem other) {
        boolean thisIsProduct = this.purchasable instanceof Product;
        boolean otherIsProduct = other.purchasable instanceof Product;

        if (thisIsProduct && !otherIsProduct) return -1;
        if (!thisIsProduct && otherIsProduct) return 1;

        if (thisIsProduct) {
            String thisName = ((Product) this.purchasable).getName();
            String otherName = ((Product) other.purchasable).getName();
            return thisName.compareTo(otherName);
        }

        return this.purchasable.getId().toString()
                .compareTo(other.purchasable.getId().toString());
    }

    public double getDiscountApplied() {
        return discountApplied;
    }

    public void setDiscountApplied(double discountApplied) {
        this.discountApplied = discountApplied;
    }
}
